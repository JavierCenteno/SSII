# -*- coding: utf-8 -*-
"""
This is the main file for this application.

@author: Javier Centeno Vega
@author: Antonio Millán García
"""

import codecs
import json
import os

# Password, used to encrypt/decrypt hash files
password = input("Please write your password")
# Program configuration
configuration = json.loads(codecs.open("configuration.json", "r", encoding="utf8").read())

# TODO:
# If this is the first time this script is executed, send a warning!
# So that an attacker deletes the hash file, it is detected
# The warning should warn that the script is running for the first time and that if it's not, then the hashes have been deleted and there's a possibility that the files have been tampered with

