# -*- coding: utf-8 -*-
"""
This is the main file for this application.

@author: Javier Centeno Vega
@author: Antonio Millán García
"""

import codecs
import json
import os
import base64
import time
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.kdf.pbkdf2 import PBKDF2HMAC
from cryptography.fernet import Fernet
import hashlib
import datetime
import threading

configuration_file_path = "configuration.json"

password = input("Please write your password: ").encode()

configuration = json.loads(codecs.open(configuration_file_path, "r", encoding="utf8").read())

hashes_file_path = configuration["hashes_file_path"]
security_file_path = configuration["security_file_path"]
scanned_directories = configuration["scanned_directories"]
scanned_extensions = configuration["scanned_extensions"]
check_frequency = int(configuration["check_frequency"])
report_frequency = int(configuration["report_frequency"])

encryption_kdf = PBKDF2HMAC(algorithm = hashes.SHA256(), length = 32, salt = b"salt_", iterations = 100000, backend = default_backend())
encryption_key = base64.urlsafe_b64encode(encryption_kdf.derive(password))
encryption_fernet = Fernet(encryption_key)

def get_algorithm():
	name = configuration["algorithm"]
	if name == "SHA-224":
		return hashlib.sha224()
	if name == "SHA-256":
		return hashlib.sha256()
	if name == "SHA-384":
		return hashlib.sha384()
	if name == "SHA-512":
		return hashlib.sha512()
	if name == "MD5":
		return hashlib.md5()
	raise ValueError("The algorithm specified on configuration.json is not correct, choose one of the available options.")

def load_encrypted_file(file_path):
	if os.stat(file_path).st_size != 0:
		file = open(file_path, "rb")
		encrypted_data = file.read()
		try:
			data = encryption_fernet.decrypt(encrypted_data)
		except:
			raise ValueError("The password is not correct. The data couldn't be decrypted.")
		file.close()
	else:
		data = b""
	return data.decode("utf-8")

def save_encrypted_file(file_path, data):
	file = open(file_path, "wb")
	encrypted_data = encryption_fernet.encrypt(data.encode())
	file.write(encrypted_data)
	file.close()

def hash_file(file_path):
	hash = get_algorithm()
	with open(file_path, "rb") as f:
		for chunk in iter(lambda: f.read(4096), b""):
			hash.update(chunk)
	return hash.hexdigest()

def calculate_hashes():
	hashes_dict = {}
	totalfiles = 0
	scanned_files = []
	for directory in scanned_directories:
		scanned_files += [name for name in os.listdir(directory) if os.path.isfile(name)]
	scanned_files += [name for name in os.listdir(os.path.abspath(os.sep)) if os.path.isfile(name) and name.endswith(tuple(scanned_extensions))]
	for directory in scanned_directories:
		totalfiles = totalfiles + len([name for name in os.listdir(directory) if os.path.isfile(name)])
		for file in os.listdir(directory):
			if os.stat(file).st_size != 0:
				hashes_dict[file] = hash_file(file)
			else:
				hashes_dict[file] = 0
	return (hashes_dict,totalfiles)

def load_hashes():
	f = load_encrypted_file(hashes_file_path)
	if f != "":
		hashes_map = json.loads(f)
	else:
		hashes_map = {}
	return hashes_map

def save_hashes(hashes_map):
	hashes_map_string = json.dumps(hashes_map)
	save_encrypted_file(hashes_file_path, hashes_map_string)

def load_security_report():
	f = str(load_encrypted_file(security_file_path))
	return f

def save_security_report(report):
	save_encrypted_file(security_file_path, report)

def add_to_security_report(report):
	security_report = load_security_report()
	security_report += report
	save_security_report(security_report)

if not os.path.isfile(hashes_file_path):
	print("The hash file doesn't exist. If this is not the first time you've run this program, it means it's been deleted and your files may have been tampered with.")
else:
	old_hashes = load_encrypted_file(hashes_file_path)
	if os.stat(hashes_file_path).st_size == 0:
		print("The hash file is empty. If this is not the first time you've run this program, it means it's been deleted and your files may have been tampered with.")

if not os.path.isfile(security_file_path):
	print("The security file doesn't exist. If this is not the first time you've run this program, it means it's been deleted and your files may have been tampered with.")
else:
	if os.stat(security_file_path).st_size == 0:
		print("The security file is empty. If this is not the first time you've run this program, it means it's been deleted and the reports may have been lost.")
	old_hashes = load_hashes()

def main_loop():
	while True:
		modified=[]
		old_hashes = load_hashes()
		(new_hashes,total) = calculate_hashes()
		for filename in new_hashes:
			if filename not in old_hashes:
				add_to_security_report(str(datetime.datetime.now()) + "|NEW INCIDENCE|" + filename + " has been added.\n")
		for filename in old_hashes:
			if filename not in new_hashes:
				add_to_security_report(str(datetime.datetime.now()) + "|NEW INCIDENCE|" + filename + " has been deleted.\n")
				modified.append(filename)
			if new_hashes[filename] != old_hashes[filename]:
				if filename!=security_file_path and filename != hashes_file_path:
					add_to_security_report(str(datetime.datetime.now()) + "|NEW INCIDENCE|" + filename + " has been modified.\n")
					modified.append(filename)
		save_hashes(new_hashes)
		add_to_security_report(str(100 - 100 * len(set(modified)) / total) + "% of files have NOT been modified or deleted in the last day.\n")
		time.sleep(check_frequency)

def main_loop_report():
	while True:
		time.sleep(report_frequency)
		print("------------- MONTHLY REPORT -------------")
		print(str(load_encrypted_file(security_file_path)))
		with open(security_file_path, "w"):
			pass

t1 = threading.Thread(target = main_loop)
t1.start()
t2 = threading.Thread(target = main_loop_report)
t2.start()
