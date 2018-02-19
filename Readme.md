Blowfish
========
Blowfish is a Terminal Management System (TMS) for managing POS devices.

Tran Types
==========
log to transaction table
------------------------
Notify transaction status
Card Purchase
Cash Advance
Refund
Deposit
Purchase with Cashback
Balance Inquiry
Link Account Inquiry
Mini Statement
Fund Transfer
Card Recharge
Bill Payments
Prepaid
Purchase with Additional Data
POS Pre – Authorization
POS Pre – Authorization Completion
PIN Change


Log all requests to NoSQL
-------------------------
authenticate transaction
Biller List Download
Product List Download
Biller Subscription Information Download
Payment Validation
Terminal Master Key Download
Terminal Session Key
Terminal PIN Key
Initial PIN Encryption Key Download / Track2 Data
Initial PIN Encryption Key Download / EMV
Terminal Parameter Download
Call home
CA Public Key Download
EMV Application AID Download
Dynamic Currency Conversion


Daily Transaction Report Download




Agent Cash out
Agent Cash in
Agent Bill Payment
Agent Recharge



Tranasction flow
================
Card Purchase/Balance/Deposit/Mini Statement/CashBack
-----------------------------------------------------
Model 1(Arca)
    Step 1. POS(REST) -> messaging interface -> preprocessor -> transaction processor (Verify transaction)
    Step 2. POS(ISO8583) -> NIBSS(ISO8583) -> Switch -> Issuer
    Step 3. POS(REST) -> messaging interface -> preprocessor -> transaction processor (Notify transaction status)
Model 2(Arca)
    POS(REST) -> messaging interface -> preprocessor -> transaction processor -> router(ISO8583) -> NIBSS -> Switch -> Issuer
Model 2(3Line)
    POS(REST) -> messaging interface -> preprocessor -> transaction processor -> router(ISO8583) -> Switch -> Issuer


Card Bill Payment/Recharge
--------------------------
Model 1
    Step 1. POS(REST) -> messaging interface -> preprocessor -> transaction processor (Verify transaction)
    Step 2. POS(ISO8583) -> NIBSS -> Switch -> Issuer
    Step 3. POS(REST) -> messaging interface -> preprocessor -> transaction processor -> router(REST) -> VAS Service Provider (Give value & notify transaction status)
Model 2
    POS(REST) -> messaging interface -> preprocessor -> transaction processor -> router(ISO8583) -> NIBSS -> Switch -> Issuer
                                                        transaction processor -> router(REST) -> VAS Service Provider  (Give value)
    
    
    
Agent Cashout/CashIn/Balance/Deposit/Mini Statement
---------------------------------------------------
POS -> messaging interface -> preprocessor -> transaction processor -> router -> Financial Service Provider

Agent Bill Payment/Recharge
---------------------------
POS -> messaging interface -> preprocessor -> transaction processor -> router -> Financial Service Provider
                                              transaction processor -> router(REST) -> VAS Service Provider
                        

    
    
Store transactionreq &resp
    


messaging interface
    accept network message
    transform message
preprocessor
    validate acquirer
    validate merchant
    validate terminal
    validate message type
    validate transaction type
    validate PAN
    validate exp date
transaction processor
    validate transaction requirement
    pin translation
router
    based message/transaction type/BIN route message to Switch or NIBSS


Purchase/Balance/Deposit/Mini Statement/CashBack
    receives ISO8583 or REST
    sends out ISO8583 to NIBSS or Switch

Bill Payment/Recharge
    receives ISO8583 or REST
    sends out ISO8583 to NIBSS or Switch
    sends out REST to Service Provider

    
Acquirer (Enable or Disable tran type/BIN settings for all by default)
    enable transaction type
    enable BIN
    enable transaction type by merchant
    enable BIN by merchant

Merchant (Enable or Disable tran type/BIN settings for all by default)
    enable transaction type
    enable BIN
    enable transaction type by terminal
    enable BIN by terminal






merchants settings overrides terminals settings or terminals settings overrides merchants settings?
acquirers settings overrides merchants settings or merchants settings overrides acquirers settings?
Shld acquirers be able to manage terminals?




UI (Frontend)
=============
User Login
DashBoard(Acquirer, Merchant, App Owner - PTSP)
User Mgt
Role Mgt
Acquirer Mgt
Merchant Mgt
Terminal Mgt
Key Mgt
Transaction Log
TranType Mgt
BIN Mgt
Acquirer/BIN Mgt
Acquirer/Merchant Mgt
Acquirer/Merchant/TranType/Bin Mgt
Merchant/BIN Mgt
Merchant/Terminal Mgt
Merchant/Terminal/TranType/BIN Mgt
Contact Mgt
Reports (Settlement, Terminal, Merchant, Reconciliation)
Reconciliation (with NIBSS & Acqurer data)


Non-Functional Requirements
===========================
Approval Workflow
Security
Key Management (TMK, Terminal Public Key)


APIs (Backend)
=============
Domains
-------
Create Domain
Get Domains
Get Domain
Update Domain
Delete Domain

Users
-----
Create User
Get Users
Get User
Update User
Delete User

Roles
-----
Create Role
Get Roles
Get Role
Update Role
Delete Role

Permissions
-----------
Create Permission
Get Permissions
Get Permission
Update Permission
Delete Permission

Acquirers
---------
Create Acquirer
Get Acquirers
Get Acquirer
Update Acquirer
Delete Acquirer
DashBoard
Settlement Reports
Merchant Reports
Terminal Reports

Merchants
---------
Create Merchant
Get Merchants
Get Merchant
Update Merchant
Delete Merchant
DashBoard
Settlement Reports
Terminal Reports

Terminals
---------
Create Terminal
Get Terminals
Get Terminal
Update Terminal
Delete Terminal
DashBoard
Settlement Reports
Merchant Reports

Keys
----
Create Key
Get Keys
Get Key
Update Key
Delete Key

Transactions
------------
Create Transaction
Get Transactions
Get Transaction
Update Transaction

TranType
--------
Create TranType
Get TranTypes
Get TranType
Update TranType

BINs
----
Create BIN
Get BINs
Get BIN
Update BIN

Contacts
--------
Create Contact
Get Contacts
Get Contact
Update Contact


Terminal Parameters
===================
NIBSS Host, Port, Timeout, SSL
CTMK Key
BDK Key
Key Download time
Key Download Interval Check time
Terminal Type
Terminal Capabilities
Terminal Extra Capabilities
Transaction Currency
Transaction Reference Currency
Force Online
POS Data Code
ICC Data

Acquirer by Terminal Parameter Grp
Merchant by Terminal Parameter Grp
Terminal by Terminal Parameter Grp

App Ops
=========
Create Docker image
Cache to Redis
Log to Kafka (Metrics)
Unit test


