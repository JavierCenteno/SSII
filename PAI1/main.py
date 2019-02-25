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

# Password, used to encrypt/decrypt hash files
password = input("Please write your password: ").encode()
# Program configuration
configuration = json.loads(codecs.open("configuration.json", "r", encoding="utf8").read())
# Create key
encryption_kdf = PBKDF2HMAC(algorithm=hashes.SHA256(), length=32, salt=b'salt_', iterations=100000, backend=default_backend())
encryption_key = base64.urlsafe_b64encode(encryption_kdf.derive(password))
encryption_fernet = Fernet(key)

# TODO:
# encrypt using encryption_fernet.encrypt(data)
# decrypt using encryption_fernet.decrypt(data)
# can pass the encrypt and decrypt functions data straight from open(filename, "r").read(), and write that data straight to open(filename, "w").write()

# TODO:
# If the hashes file doesn't exist, it may mean either this is the first time this script is being run or an attacker has deleted the hashes. Send a warning!
# The warning should warn that the script is running for the first time and that if it's not, then the hashes have been deleted and there's a possibility that the files have been tampered with.
