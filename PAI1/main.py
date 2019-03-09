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

# Path to the configuration file
configuration_file_path = "configuration.json"

# Password, used to encrypt/decrypt hash files
password = input("Please write your password: ").encode()

# Program configuration
configuration = json.loads(codecs.open(configuration_file_path, "r", encoding="utf8").read())

# Load configuration
hashes_file_path = configuration["hashes_file_path"]
security_file_path = configuration["security_file_path"]
scanned_directories = configuration["scanned_directories"]
check_frequency = int(configuration["check_frequency"])
report_frequency = int(configuration["report_frequency"])

# Create key
encryption_kdf = PBKDF2HMAC(algorithm = hashes.SHA256(), length = 32, salt = b"salt_", iterations = 100000, backend = default_backend())
encryption_key = base64.urlsafe_b64encode(encryption_kdf.derive(password))
encryption_fernet = Fernet(encryption_key)

# Select hashing algorithm
algorithms = {
	"SHA-224": hashlib.sha224(),
	"SHA-256": hashlib.sha256(),
	"SHA-384": hashlib.sha384(),
	"SHA-512": hashlib.sha512(),
	"MD5": hashlib.md5()
}

algorithm = algorithms[configuration["algorithm"]]

# Start threads
# Functions to load and save encrypted files, encrypting or decrypting the data to be saved in the process

def load_encrypted_file(file_path):
	if os.stat(file_path).st_size != 0:
		file = open(file_path, "rb")
		encrypted_data = file.read()
		data = encryption_fernet.decrypt(encrypted_data)
		file.close()
	else:
		data = ""
	return data

def save_encrypted_file(file_path, list):
	file = open(file_path, "wb")
	data = ""
	for l in list:
		data = data + str(l) + "\n"
	encrypted_data = encryption_fernet.encrypt(data.encode())
	file.write(encrypted_data)
	file.close()
# TODO: hash files and store the hashes in new_hashes

def hash_file(file):
	hash = algorithm
	with open(file, "rb") as f:
		# Reads file in chunks of 4096 bytes, doesn't have to load whole file on memory, which can be problematic with very big files.
		for chunk in iter(lambda: f.read(4096), b""):
			hash.update(chunk)
	return hash.hexdigest()

def load_new_hashes():
	hashes_list = []
	for directory in scanned_directories:
		for file in os.listdir(directory):
			print(file)
			if os.stat(file).st_size != 0:
				hashes_list.append(hash_file(file))
	return hashes_list

def load_old_hashes():
	hashes = []
	f = str(load_encrypted_file(hashes_file_path))
	print(f)
	for line in f:
		hashes.append(line)
	return hashes

old_hashes = None

if not os.path.isfile(hashes_file_path):
	# If the hashes file doesn't exist, it may mean either this is the first time this script is being run or an attacker has deleted the hashes.
	print("The hash file doesn't exist. If this is not the first time you've run this program, it means it's been deleted and your files may have been tampered with.")
else:
	old_hashes = load_encrypted_file(hashes_file_path)

if not os.path.isfile(security_file_path):
	# If the security file doesn't exist, it may mean either this is the first time this script is being run or an attacker has deleted the file.
	print("The security file doesn't exist. If this is not the first time you've run this program, it means it's been deleted and your files may have been tampered with.")
else:
	old_hashes = load_old_hashes()

def load_file_names():
	names = []
	for directory in scanned_directories:
		for file in os.listdir(directory):
			names.append(file)
	return names

def main_loop():
	while True:
		old_hashes = load_old_hashes()
		new_hashes = load_new_hashes()
		filenames = load_file_names()
		n = 0
		print("New hashes: " + str(new_hashes))
		print("Old hashes: " + str(old_hashes))
		for i in range(len(old_hashes)):
			if new_hashes[i] != old_hashes[i]:
				file = open(security_file_path, "w")
				file.write(datetime.datetime.now() + "|NEW INCIDENCE|" + filenames[i] + " has been modified without permission.")
			n = n + 1
		save_encrypted_file(hashes_file_path,new_hashes)
		# Time check of configuration.json
		time.sleep(check_frequency)

def main_loop_report():
	while True:
		# Time report of configuration.json
		time.sleep(report_frequency)
		# Send report

t1 = threading.Thread(target=main_loop)
t1.start()
t2 = threading.Thread(target=main_loop_report)
t2.start()

# TODO: Give 2-3 options at least for hash algorithms
