-- Domains
INSERT INTO tbl_domains (name, description, status, created_by, created_on) VALUES ('Platform Owner', 'Platform Owner', 1, 'System', CURRENT_TIMESTAMP())
INSERT INTO tbl_domains (name, description, status, created_by, created_on) VALUES ('Acquirer', 'Acquiring financial institution', 1, 'System', CURRENT_TIMESTAMP())
INSERT INTO tbl_domains (name, description, status, created_by, created_on) VALUES ('Merchant', 'Merchant', 1, 'System', CURRENT_TIMESTAMP())

-- Acquirers
INSERT INTO tbl_acquirers (name, code, cbn_code, address, phoneNo, domain_id, enable_all_tran_type, enable_all_bin, description, status, created_by, created_on) VALUES ('Access bank', '044', '044', 'Head Office, Plot 999c, Danmole Street, Off Adeola Odeku/Idejo Street, Victoria Island, Lagos, Nigeria', '+234 1- 2712005-7', 2, 1, 1, 'Access Bank', 1, 'System', CURRENT_TIMESTAMP())
INSERT INTO tbl_acquirers (name, code, cbn_code, address, phoneNo, domain_id, enable_all_tran_type, enable_all_bin, description, status, created_by, created_on) VALUES ('GTBank', '057', '057', '635 Akin Adesola Street, Victoria Island, Lagos', '+234 1 448 0000', 2, 1, 1, 'Guaranty Trust Bank', 1, 'System', CURRENT_TIMESTAMP())

-- Transaction Types
INSERT INTO tbl_transaction_types (name, code, description, status, created_by, created_on) VALUES ('Purchase', '00', 'Purchase of Goods & Services', 1, 'System', CURRENT_TIMESTAMP())

-- BINs
-- https://en.wikipedia.org/wiki/Payment_card_number
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('4', 'All Visa card', 1, CURRENT_TIMESTAMP(), 'System')
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('50', 'Maestro 50', 1, CURRENT_TIMESTAMP(), 'System')
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('56', 'Maestro 56', 1, CURRENT_TIMESTAMP(), 'System')
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('57', 'Maestro 57', 1, CURRENT_TIMESTAMP(), 'System')
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('58', 'Maestro 58', 1, CURRENT_TIMESTAMP(), 'System')
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('51', 'MasterCard 51', 1, CURRENT_TIMESTAMP(), 'System')
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('52', 'MasterCard 52', 1, CURRENT_TIMESTAMP(), 'System')
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('53', 'MasterCard 53', 1, CURRENT_TIMESTAMP(), 'System')
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('54', 'MasterCard 54', 1, CURRENT_TIMESTAMP(), 'System')
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('55', 'MasterCard 55', 1, CURRENT_TIMESTAMP(), 'System')
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('506', 'Verve', 1, CURRENT_TIMESTAMP(), 'System')
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('62', 'China Union Pay', 1, CURRENT_TIMESTAMP(), 'System')
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('64', 'Discover Card 64', 1, CURRENT_TIMESTAMP(), 'System')
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('65', 'Discover Card 65', 1, CURRENT_TIMESTAMP(), 'System')
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('6011', 'Discover Card 6011', 1, CURRENT_TIMESTAMP(), 'System')
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('34', 'American Express 34', 1, CURRENT_TIMESTAMP(), 'System')
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('37', 'American Express 37', 1, CURRENT_TIMESTAMP(), 'System')


-- Users (test purpose)
INSERT INTO tbl_users (username, password, name, status, created_on, created_by)  VALUES ('admin', 'password', 'admin', 1, CURRENT_TIMESTAMP(), 'System')

-- Contacts (test purpose)
INSERT INTO tbl_contacts (fname, lname, address, phoneNo, status, created_on, created_by) VALUES ('Firstname', 'Lastname', 'No 60A,CampbellStr, Lagos Island, Lagos', '08055475028', 1, CURRENT_TIMESTAMP(), 'System')

-- Key Versions (test purpose)
INSERT INTO tbl_key_versions (data, check_digit, version, usage, algo, salt, description, status, created_by, created_on) VALUES ('DBEECACCB4210977ACE73A1D873CA59F', '1DDD47', '1.0', 2, 'TDES', '1010101010101010', 'Encrypt Key version 1', 1, 'System', CURRENT_TIMESTAMP())
INSERT INTO tbl_key_versions (data, check_digit, version, usage, algo, salt, description, status, created_by, created_on) VALUES ('DBEECACCB4210977ACE73A1D873CA59F', '1DDD47', '2.0', 3, 'SHA512', '1010101010101010', 'Encrypt Key version 1', 1, 'System', CURRENT_TIMESTAMP())

-- Merchants (test purpose)
INSERT INTO tbl_merchants (name, code, address, phone_no, domain_id, enable_all_tran_type, enable_all_bin, description, status, created_by, created_on) VALUES ('Test Merchant', 'TESTMERCHANT001', 'Merchant Address', '08055475028', 3, 1, 0, 'Test Merchant', 1, 'System', CURRENT_TIMESTAMP())

-- Terminals (test purpose)
INSERT INTO tbl_terminals (code, device_serial_no, terminal_parameter_group, description, status, created_on, created_by)
VALUES ('20390059', 'P352701711018088', 1, 'Terminal', 1, CURRENT_TIMESTAMP(), 'System')

-- Terminal Parameters (test purpose)
INSERT INTO tbl_terminal_parameters (name, tms_endpoint_id, ctmk_key_id, bdk_key_id, acquirer_id, key_download_time_in_min, key_download_interval_in_min, terminal_type, terminal_capabilities, terminal_extra_capabilities, transaction_currency, transaction_reference_currency, force_online, pos_data_code, icc_data, description, status, created_on, created_by) VALUES ('DefaultGroup', 1, 1, 1, 1, 1440, 60, 22, 'E090C8', '7F80C0F0FF', '0566', '0566', true, '510101511344101', '9F26,9F27,9F10,9F37,9F36,95,9A,9C,9F02,5F2A,82,9F1A,9F34,9F33,9F35,9F03', 'Default Terminal Parameter Download group', 1, CURRENT_TIMESTAMP(), 'System')

-- Merchants (test purpose)
INSERT INTO tbl_endpoints (ip, port, timeout, ssl, description, status, created_by, created_on) VALUES ('196.6.103.72', 5042, 60, false, 'NIBSS CTMS', 1, 'System', CURRENT_TIMESTAMP())


-- 20390059

-- TO_DATE('2017-10-14', 'yyyy-mm-dd')

