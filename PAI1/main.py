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
hashes_file_path = "hashes"

# Password, used to encrypt/decrypt hash files
password = input("Please write your password: ").encode()

# Program configuration
configuration = json.loads(codecs.open(configuration_file_path, "r", encoding="utf8").read())

# Create key
encryption_kdf = PBKDF2HMAC(algorithm = hashes.SHA256(), length = 32, salt = b"salt_", iterations = 100000, backend = default_backend())
encryption_key = base64.urlsafe_b64encode(encryption_kdf.derive(password))
encryption_fernet = Fernet(key)

# Loads and decrypts the hashes file.
def load_hashes():
	file = open(hashes_file_path, "r")
	encrypted_data = file.read()
	data = encryption_fernet.decrypt(encrypted_data)
	file.close()
	return data

# Encrypts and saves the hashes file.
def save_hashes(hashes):
	file = open(hashes_file_path, "w")
	encrypted_data = encryption_fernet.encrypt(hashes)
	file.write(encrypted_data)
	file.close()



# TODO:
# If the hashes file doesn't exist, it may mean either this is the first time this script is being run or an attacker has deleted the hashes. Send a warning!
# The warning should warn that the script is running for the first time and that if it's not, then the hashes have been deleted and there's a possibility that the files have been tampered with.
