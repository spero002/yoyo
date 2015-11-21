# -*- coding: utf-8 -*-
import hashlib, json, httplib
import urlparse, urllib
import sys
from config import *

class ApiException(Exception):
    def __str__(self):
        return "Request api Error!"

# calculate the signation use private key and params
def _verfy_ac(private_key, params):
    #print params
    items = params.items()
    items.sort()
    #print items

    params_data = ""
    for key, value in items:
        params_data = params_data + str(key) + str(value)
        #print params_data

    params_data = params_data+private_key
    #print params_data
    
    '''use sha1 to encode keys'''
    hash_new = hashlib.sha1()
    hash_new.update(params_data)
#    print hash_new.update(params_data)
    hash_value = hash_new.hexdigest()
    return hash_value


class ApiConnection(object):
    # build connection accoriding to base url type
    def __init__(self, base_url):
        self.base_url = base_url
        o = urlparse.urlsplit(base_url)
        if o.scheme == 'https':
            self.conn = httplib.HTTPSConnection(o.netloc)
        else:
            self.conn = httplib.HTTPConnection(o.netloc)

    # close connect
    def __del__(self):
        self.conn.close()

    # get response from api server
    def get(self, resouse, params):
        resouse += "?" + urllib.urlencode(params)
        print("%s%s" % (self.base_url, resouse))
        self.conn.request("GET", resouse)
        response = json.loads(self.conn.getresponse().read())
        return response

class ApiClient(object):
    def __init__(self, base_url, public_key, private_key):
        self.g_params = {}
        self.g_params['PublicKey'] = public_key
        self.private_key = private_key
        self.conn = ApiConnection(base_url)

    def get(self, uri, params):
        # print params
        _params = dict(self.g_params, **params)
        _params["Signature"] = _verfy_ac(self.private_key, _params)
        return self.conn.get(uri, _params)
