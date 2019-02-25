# -*- coding: utf-8 -*-
"""
This is the main file for this application.

@author: Javier Centeno Vega
@author: Antonio Millán García
"""

import codecs
import json
import os

configuration = json.loads(codecs.open("configuration.json", "r", encoding="utf8").read())

