-- Domains
INSERT INTO tbl_domains (code, name, description, status, created_by, created_on) VALUES ('OWNER', 'Arca Payment', 'Platform Owner', 1, 'System', CURRENT_TIMESTAMP());
INSERT INTO tbl_domains (code, name, description, status, created_by, created_on) VALUES ('ACQUIRER', 'Acquirer', 'Acquiring financial institution', 1, 'System', CURRENT_TIMESTAMP());
INSERT INTO tbl_domains (code, name, description, status, created_by, created_on) VALUES ('MERCHANT', 'Merchant', 'Merchant', 1, 'System', CURRENT_TIMESTAMP());

-- Acquirers
INSERT INTO tbl_acquirers (name, code, bin_code, cbn_code, address, phoneNo, domain_id, enable_all_tran_type, enable_all_bin, description, status, created_by, created_on) VALUES ('Access bank', '044', '628050', '044', 'Head Office, Plot 999c, Danmole Street, Off Adeola Odeku/Idejo Street, Victoria Island, Lagos, Nigeria', '+234 1- 2712005-7', 2, 1, 1, 'Access Bank', 1, 'System', CURRENT_TIMESTAMP());
INSERT INTO tbl_acquirers (name, code, bin_code, cbn_code, address, phoneNo, domain_id, enable_all_tran_type, enable_all_bin, description, status, created_by, created_on) VALUES ('GTBank', '057', '628051', '057', '635 Akin Adesola Street, Victoria Island, Lagos', '+234 1 448 0000', 2, 1, 1, 'Guaranty Trust Bank', 1, 'System', CURRENT_TIMESTAMP());

-- Transaction Types
INSERT INTO tbl_transaction_types (name, code, description, status, created_by, created_on) VALUES ('Purchase', '00', 'Purchase of Goods & Services', 1, 'System', CURRENT_TIMESTAMP());

-- BINs (https://en.wikipedia.org/wiki/Payment_card_number)
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('4', 'All Visa card', 1, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('50', 'Maestro 50', 1, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('56', 'Maestro 56', 1, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('57', 'Maestro 57', 1, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('58', 'Maestro 58', 1, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('51', 'MasterCard 51', 1, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('52', 'MasterCard 52', 1, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('53', 'MasterCard 53', 1, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('54', 'MasterCard 54', 1, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('55', 'MasterCard 55', 1, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('506', 'Verve', 1, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('62', 'China Union Pay', 1, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('64', 'Discover Card 64', 1, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('65', 'Discover Card 65', 1, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('6011', 'Discover Card 6011', 1, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('34', 'American Express 34', 1, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_bins (code, description, status, created_on, created_by) VALUES ('37', 'American Express 37', 1, CURRENT_TIMESTAMP(), 'System');



-- Test Data -- Do not include in production setup
-- Users (test purpose)
INSERT INTO tbl_users (username, password, name, status, created_on, created_by)  VALUES ('admin', 'password', 'admin', 1, CURRENT_TIMESTAMP(), 'System');

-- Contacts (test purpose)
INSERT INTO tbl_contacts (fname, lname, address, phoneNo, status, created_on, created_by) VALUES ('Firstname', 'Lastname', 'No 60A,CampbellStr, Lagos Island, Lagos', '08055475028', 1, CURRENT_TIMESTAMP(), 'System');

-- Key Versions (test purpose)
INSERT INTO tbl_keys (data, check_digit, version, key_usage, algo, salt, description, status, created_by, created_on) VALUES ('DBEECACCB4210977ACE73A1D873CA59F', '1DDD47', '1.0', 2, 'TDES', '1010101010101010', 'Encrypt Key version 1', 1, 'System', CURRENT_TIMESTAMP());
INSERT INTO tbl_keys (data, check_digit, version, key_usage, algo, salt, description, status, created_by, created_on) VALUES ('DBEECACCB4210977ACE73A1D873CA59F', '1DDD47', '2.0', 3, 'SHA512', '1010101010101010', 'Encrypt Key version 1', 1, 'System', CURRENT_TIMESTAMP());

-- Merchants (test purpose)
INSERT INTO tbl_merchants (name, code, address, phone_no, domain_id, enable_all_tran_type, enable_all_bin, description, status, created_by, created_on) VALUES ('Test Merchant 1', 'TESTMERCHANT001', 'Merchant Address', '08055475028', 3, 1, 0, 'Test Merchant 1', 1, 'System', CURRENT_TIMESTAMP());
INSERT INTO tbl_merchants (name, code, address, phone_no, domain_id, enable_all_tran_type, enable_all_bin, description, status, created_by, created_on) VALUES ('Test Merchant 2', 'TESTMERCHANT002', 'Merchant Address', '08055475029', 3, 1, 0, 'Test Merchant 2', 1, 'System', CURRENT_TIMESTAMP());

-- Terminals (test purpose)
INSERT INTO tbl_terminals (code, serial_no, manufacturer, model_no, build_no, os, os_version, firmware_no, description, status, created_on, created_by) VALUES ('20390059', 'P352701711018088', 'WPOS', 'WPOS-3', 'WPOS-3_V1.01_18020715_userdebug', 'Android', '5.1.1', 'SP_V1.21_B0T0_170920', 'WPOS Terminal', 1, CURRENT_TIMESTAMP(), 'System');

-- Terminal Parameters (test purpose)
INSERT INTO tbl_terminal_parameters (name, tms_endpoint_id, ctmk_key_id, bdk_key_id, key_download_time_in_min, key_download_interval_in_min, terminal_type, terminal_capabilities, terminal_extra_capabilities, transaction_currency, transaction_reference_currency, transaction_currency_exponent, reference_currency_exponent, reference_currency_conversion, force_online, pos_data_code, icc_data, support_default_tdol, support_default_ddol, support_pse_selection, use_local_network_config, description, status, created_on, created_by) VALUES ('Default', 1, 1, 1, 1440, 60, 22, 'E090C8', '7F80C0F0FF', '0566', '0566', '2', '2', '0', true, '510101511344101', '9F26,9F27,9F10,9F37,9F36,95,9A,9C,9F02,5F2A,82,9F1A,9F34,9F33,9F35,9F03', true, true, true, true, 'Default Terminal Parameter Download group', 1, CURRENT_TIMESTAMP(), 'System');

-- Merchants (test purpose)
INSERT INTO tbl_endpoints (hostname, ip, port, timeout, ssl_enabled, description, status, created_by, created_on) VALUES ('ctms.nibss-plc.com', '196.6.103.72', 5042, 60, false, 'NIBSS CTMS', 1, 'System', CURRENT_TIMESTAMP());

-- Acquirer BINS (test purpose)
INSERT INTO tbl_acquirers_bins (acquirer_id, bin_id, created_on, created_by) VALUES (1, 1, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_acquirers_bins (acquirer_id, bin_id, created_on, created_by) VALUES (2, 6, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_acquirers_bins (acquirer_id, bin_id, created_on, created_by) VALUES (2, 7, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_acquirers_bins (acquirer_id, bin_id, created_on, created_by) VALUES (2, 8, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_acquirers_bins (acquirer_id, bin_id, created_on, created_by) VALUES (2, 9, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_acquirers_bins (acquirer_id, bin_id, created_on, created_by) VALUES (2, 10, CURRENT_TIMESTAMP(), 'System');

-- Acquirer Merchants (test purpose)
INSERT INTO tbl_acquirers_merchants (acquirer_id, merchant_id, created_on, created_by) VALUES (2, 1, CURRENT_TIMESTAMP(), 'System');

-- Merchant Terminals (test purpose)
INSERT INTO tbl_merchants_terminals (merchant_id, terminal_id, created_on, created_by) VALUES (1, 1, CURRENT_TIMESTAMP(), 'System');

-- Acquirer Terminal Parameters (test purpose)
INSERT INTO tbl_acquirers_terminal_parameters (acquirer_id, term_param_id, created_on, created_by) VALUES (2, 1, CURRENT_TIMESTAMP(), 'System');

-- Merchant Terminal Parameters (test purpose)
INSERT INTO tbl_merchants_terminal_parameters (merchant_id, term_param_id, created_on, created_by) VALUES (1, 1, CURRENT_TIMESTAMP(), 'System');

-- Terminal Terminal Parameters (test purpose)
INSERT INTO tbl_terminals_terminal_parameters (terminal_id, term_param_id, created_on, created_by) VALUES (1, 1, CURRENT_TIMESTAMP(), 'System');





