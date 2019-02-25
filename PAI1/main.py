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
password = input("Please write your password: ")
# Program configuration
configuration = json.loads(codecs.open("configuration.json", "r", encoding="utf8").read())

# TODO:
# If the hashes file doesn't exist, it may mean either this is the first time this script is being run or an attacker has deleted the hashes. Send a warning!
# The warning should warn that the script is running for the first time and that if it's not, then the hashes have been deleted and there's a possibility that the files have been tampered with.

