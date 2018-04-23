Blowfish
========
Rest API for Acquirers  
* Create
* Read
* Update
* Delete  

Rest API for AcquirerBins  
* Create
* Read
* Update
* Delete

Rest API for AcquirerMerchants  
* Create
* Read
* Update
* Delete

Rest API for AcquirerTerminalParameters  
* Create
* Read
* Update
* Delete

Rest API for Merchants  
* Create
* Read
* Update
* Delete
  
Rest API for MerchantBins  
* Create
* Read
* Update
* Delete
  
Rest API for MerchantTerminal  
* Create
* Read
* Update
* Delete
  
Rest API for MerchantTerminalParameter  
 * Create
 * Read
 * Update
 * Delete
  
Rest API for Terminal  
 * Create
 * Read
 * Update
 * Delete
 
Rest API for TerminalTerminalParameter  
 * Create
 * Read
 * Update
 * Delete
 
Rest API for TerminalParameter  
* Create
* Read
* Update
* Delete

Rest API for Bin  
* Create
* Read
* Update
* Delete

Rest API for Domain  
* Create
* Read
* Update
* Delete

Rest API for Endpoint  
* Create
* Read
* Update
* Delete

Rest API for Key  
* Create
* Read
* Update
* Delete

Rest API for KeyManagement  
* Generate Keys (RSA for ZMK & 3DES for DEK)  
* Import 3DES for ZMK

Rest API for TransactionNotification  
* Create
* Read

Rest API for KeyMap (Pending)  
* Create
* Read
* Update
* Delete

Rest API for HsmEndpoint (Pending)  
* Create
* Read
* Update
* Delete

Rest API for ActiveHsm (Pending)  
* Create
* Read
* Update
* Delete

Enable Authentication - Done

Outstanding
===========
Update generate DEK under ZMK - done
Test generate ZMK with key components - done
Package HSM Emulator as Docker  
Send transaction from POS and saved in DB 
POS generation of Authentication Token and use Authentication
Send traces (Requests, meta-dda) to Kafka  
Send exceptions to Kafka  
Ensure Caching to redis works - install Redis
Add generate ZMK/KEK

 
 3 things to worry about  
 generate random key components
 changing HSM - easy, changing ZMK - hard, changing DEK - hard  
 
 
 -- Test ZMK
 -- ZMK Component
 -- Component 1 - 0723CD4CDDF25A1F8F3517EEABE482C9, E2AC18
 -- Component 2 - 227A6A90957517C2E22CBC934EC55187, 1E656F
 -- Clear ZMK - 2559A7DC48874DDD6D19AB7DE521D34E, 7FF5B8
 -- Encrypted ZMK (Under Test LMK) - AAB9188201281F3780DC7915EDD413C1, 7FF5B8CDB7E70000
 -- NIBSS ZMK
 -- Clear NIBSS ZMK - DAEFCBCDB5200876ADE63B1C863DA49E
 -- Encrypted ZMK (Under Test LMK) - 952392F5CB403F240719CB199CFDC511
 
 