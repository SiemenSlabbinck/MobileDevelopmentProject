#Sript to initialize firebase toilet database
import json
from urllib.request import urlopen

import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

try:
    from types import SimpleNamespace as Namespace
except ImportError:
    from argparse import Namespace

cred = credentials.Certificate("serviceAccountKey.json")
firebase_admin.initialize_app(cred)

db=firestore.client()
  
class Toilet(object):
    def __init__(self, street):
        self.street = street

def toilet_decoder(obj):
    return Toilet(obj['features']['street'])

# import json

url = "https://geodata.antwerpen.be/arcgissql/rest/services/P_Portal/portal_publiek1/MapServer/8/query?outFields=*&where=1%3D1&f=geojson"
  
# store the response of URL
response = urlopen(url)
  
# storing the JSON response 
# from url in data
data_json = json.loads(response.read(), object_hook = lambda d : Namespace(**d))

class NewToilet:
	def __init__(self, street, number, postal_code, district, target_audience, wheelchair_accessible, changing_table, x_coord, y_coord, email):
		self.street = street
		self.number = number
		self.postal_code = postal_code
		self.district = district
		self.target_audience = target_audience
		self.wheelchair_accessible = wheelchair_accessible
		self.changing_table = changing_table
		self.x_coord = x_coord
		self.y_coord = y_coord
		self.email = email
	
	def to_dict(self):
		dest = {
            u'street': self.street,
            u'number': self.number,
            u'postal_code': self.postal_code,
			u'district': self.district,
			u'target_audience': self.target_audience,
			u'wheelchair_accessible': self.wheelchair_accessible,
			u'changing_table': self.changing_table,
			u'x_coord': self.x_coord,
			u'y_coord': self.y_coord,
			u'email': self.email
        }
		return dest	


# print the json response
for data in data_json.features:
	toilet = NewToilet(
		data.properties.STRAAT,
		data.properties.HUISNUMMER,
		data.properties.POSTCODE,
		data.properties.DISTRICT,
		data.properties.DOELGROEP,
		data.properties.INTEGRAAL_TOEGANKELIJK,
		data.properties.LUIERTAFEL,
		data.geometry.coordinates[1],
		data.geometry.coordinates[0],
		data.properties.CONTACTPERSOON
	)
	db.collection("toilets").add(toilet.to_dict())
	print("Document added")
