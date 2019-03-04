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
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.kdf.pbkdf2 import PBKDF2HMAC
from cryptography.fernet import Fernet

# Path to the configuration file
configuration_file_path = "configuration.json"

# Path to the file which stores the encrypted hashes of files
hashes_file_path = None

# Path to the file which stores the security warnings
security_file_path = None

# Password, used to encrypt/decrypt hash files
password = input("Please write your password: ").encode()

# Program configuration
configuration = json.loads(codecs.open(configuration_file_path, "r", encoding="utf8").read())

hashes_file_path = configuration["hashes_file_path"]
security_file_path = configuration["security_file_path"]

# Create key
encryption_kdf = PBKDF2HMAC(algorithm = hashes.SHA256(), length = 32, salt = b"salt_", iterations = 100000, backend = default_backend())
encryption_key = base64.urlsafe_b64encode(encryption_kdf.derive(password))
encryption_fernet = Fernet(key)

# Functions to load and save encrypted files, encrypting or decrypting the data to be saved in the process

def load_encrypted_file(file_path):
	file = open(file_path, "r")
	encrypted_data = file.read()
	data = encryption_fernet.decrypt(encrypted_data)
	file.close()
	return data

def save_encrypted_file(file_path, data):
	file = open(file_path, "w")
	encrypted_data = encryption_fernet.encrypt(data)
	file.write(encrypted_data)
	file.close()

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
	old_hashes = load_hashes(security_file_path)

# TODO: hash files and store the hashes in new_hashes
new_hashes = None

# TODO: compare old_hashes and new_hashes

# TODO: Give 2-3 options at least for hash algorithms
