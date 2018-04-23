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

INSERT INTO tbl_key_cryptographic_types (code, description, status, created_by, created_on) VALUES ('RSA', 'RSA Keys', 1, 'System', CURRENT_TIMESTAMP());
INSERT INTO tbl_key_cryptographic_types (code, description, status, created_by, created_on) VALUES ('3DES', 'Triple DES Keys', 1, 'System', CURRENT_TIMESTAMP());

INSERT INTO tbl_key_usage_types (code, parent_allowed, description, status, created_by, created_on) VALUES ('LMK', 0, 'Local Master Keys (LMK)', 1, 'System', CURRENT_TIMESTAMP());
INSERT INTO tbl_key_usage_types (code, parent_allowed, description, status, created_by, created_on) VALUES ('ZMK', 0, 'Zone Master Keys (ZMK)', 1, 'System', CURRENT_TIMESTAMP());
INSERT INTO tbl_key_usage_types (code, parent_allowed, description, status, created_by, created_on) VALUES ('ZPK', 1, 'Zone Pin Keys (ZPK)', 1, 'System', CURRENT_TIMESTAMP());
INSERT INTO tbl_key_usage_types (code, parent_allowed, description, status, created_by, created_on) VALUES ('DEK', 1, 'Data Encryption Keys (DEK)', 1, 'System', CURRENT_TIMESTAMP());

-- Key Versions (test purpose)
-- Auth = 1, Encrypt = 2, Sign (Hash) = 3
-- Test ZMK
-- ZMK Component
-- Component 1 - 0723CD4CDDF25A1F8F3517EEABE482C9, E2AC18
-- Component 2 - 227A6A90957517C2E22CBC934EC55187, 1E656F
-- Clear ZMK - 2559A7DC48874DDD6D19AB7DE521D34E, 7FF5B8
-- Encrypted ZMK (Under Test LMK) - AAB9188201281F3780DC7915EDD413C1, 7FF5B8CDB7E70000
-- NIBSS ZMK
-- Clear NIBSS ZMK - DAEFCBCDB5200876ADE63B1C863DA49E
-- Encrypted ZMK (Under Test LMK) - 952392F5CB403F240719CB199CFDC511
-- INSERT INTO tbl_keys (data, check_digit, key_cryptographic_type_id, key_usage_type_id, description, status, created_by, created_on) VALUES ('05403082010A028201010081334C516A9AA8290817C385A7DD385C178156ACC54F7AB4DC362161CD27517507EC30BD0F9448DE859A2727834268D72485C693F11C3EE1AB4D3EF67B3E566BC66FC14C231F45E17CAD66CA5C103030C24348DC1551D62039BC55743336ED3DFC4E7F59776882E15ACCBD356EC07A7FB859270D2356243447D4D7CF6C8E8938C9D871E92CA768E1CDDDEA4F8DAF0853E4ACCFC514FDF1E20A7F2C580A596A4288BD520947C1ABD20576C04E29C4AAC70FB9761098EADA23AA585483BB0DCB40D277461A589C76C3304624024638A61A63E5F5054710395060504B337589F738AEA7826AD42C9A368E0C2275F408922182A9E680CA67669B9159A2993281E78902030100011312E7F92FF577E00595F8A36EE591C5C80A74DF259124CCC635078E4632D1EC3BF272634A33EBF738212E567D53F39B445E3530FCC45FA690E8B041921751981347EB9F2FF8BEAFBD48062727DD79CC004EFAA2E944ACE4F2E59F947B4A3DB3BDC8DB33C4773670D7DD841EA5336F4B93541FC87BE7E1D1FC03513AA7E592C95A53E3F7A6216DD7BA97DC49774F388722B016E0024A40CFFB87487F74529DBA77ADB52F3615F8192F8ACD2321E10146289DF24AFDE1285EEDDA6F49F672C6904EC1B012DD8CBA5DF01A110DE82C7B3805BECF14D604BC7BD270EDBEF6B925491360CC889FA1CFA556E7ED27B73F9E93FADCE027CDBBF291C1142CA4979CC7B15121A1D2A6B2ACEAA00A533E587FAFD5C1924423784749AB378A912236D9B1322D143FC49DEEAC4A9F34510330489FD81EE81D16C06D1C9E194FB89A11686AE1713006A7E6829E86D63BC8181E2D3E701D8E2311E5F049E2A42366B0A2B76131AE7C3A976ECCC2E83BC81749A9D1121E863F15D42B6C9A31C8CE23D7ED42BCC29E343DBC31BDDCFAA36A99C2E99145ED21A648F06E82998B1C0136F8FE626EF56904466CACEF8C916E2C4AF352373B505C8E6E37AFC74C942E269781DC9CBA9EF7274A376F2469EABCB27361251F55A74E235562C50468D7B14925D167166D12C4483D3D60BC88AFBB5D26034E9FA14986016CA36A1500B5D67C2025F8F627077FE4995A4E54B7B1AE9826578C323DBDA631EA3AA2FE955B03631E4404EAB9B65DB39CFC098420AF341BC08042A9597DD11FD791F918B84495EC0697C5CD98D253979926AA8EB9F66F24A429168447452A2FB21BD4EC9EF6C337C07438E0D389DC688C1C54D5210A5D259CB9E3A4AC20D6366320E0824AA89D7C0F61F8091DF5973E5BADDBBB20C7DDF8967C8DF2C9588E39', '', 1, 2, 'TMS HSM RSA Key', 1, 'System', CURRENT_TIMESTAMP());
INSERT INTO tbl_keys (data, value_under_parent, check_digit, key_cryptographic_type_id, key_usage_type_id, description, status, created_by, created_on) VALUES ('3082010A0282010100999BC64EC1B2189A63AA3C15CFB1B0D5364CDB517779D0358035F0AF64324C875F3052104DC6D12F94155462AFCE4A2015CE0D4104532B85CBD8A7AE774167D3E9CBED7197E5C3C1B0FD1347C34CC6D86EDA7D819937FB8D8D6A1DB7C25ADDBC01F03A23E48A8254D83818BF4EDC8346C524B015768D5BA69825884A9CF150AE178F3307C4DE295D97A6861D7B5C2A1EB2579547856B61B4CD48F7B5839707726E8458B99019C0E5D86B59A1063B9CE594D17F92412341162B935140A657C598AE9FFB9A73C6DF26783663EB2E9120C0AAB1829FC816F730CCBF5BB4C0CA4164789AC7CDFFF5681D6CB71387FD841795F69561CC8AB50F4589752146CBE8D4FB0203010001', '2CA5FEEAD0861D8887895B8F512AF1C718C645A081128A0FACB6021D5FEC78ED86B2E37AACE5BC6C4320484947D67A1DD57B639AF23C2F674158292EBF6F3099B1E5CADC4690DBF94DDD4BBDBBFC2DC9BC7EA083ED9F205D7F5901FC456082B50F726CC88E11B025DA711C06251B90678EF7167834A481B75B50F3B65B10E1909C2D5BDF86523CE11C2ED9913CBC00DDC0DFADEBDE895CCD3D0236F5156F62E7E16F82D6F0AC7B41F89566E1216ABBAC63A59A1113FCA56E4571512A81D0D09CB7C497217382FB177C2FCF2728C4C02BBC67D483031091322C4A42C6E328B301ABF0DD52F2AF93EE8CF0201F3C6EACE50C5791C1E57490C1E99871D66C9CD652AFCC2F1D8164BCC64B993D3EEFF521F17BAEAFCF7D5CCA3D4803C70A9ECB60A82B17D5E0CABB12FB54305F4050D8C47988FA34175C22805C094CAF26F307221D882299FCAE9B7EDC871BAAD1517E159742865256446609A0F6BF28183AC18932E0153EC82158495967658D590868998030C03DAE44923DF3004094F83FB41C037E0DE987E240D29C756F6079261866EFFE26B7842C853F3C9ADDD81B939C8B8F512547DEBE193112C9CDDCD617C9E8B3DC0A70516BF8A0983F97D53511920CE22571969299EA6BF0784904436FBC98C460253922DE8ED7BF14F211CAD1652969E1C22942EF7CB6DCEFACA43DD0922B657547B36E490850A33F8AD166EACF1C8A6FC62B7B9DC8B1EE8774EA4DD23394B85BEB5AABDCC624C952B789E8493BE46CF2D48D9671D9FA67E0553E505E10934B33509F8CC214B12BBB676BB77A68727DC3C4DE9D2712CED123DEB24840A9756D7FB24191C6C64DC3A9C9C66C57460096BF8C9ABCC7E129C16DB63F082704D488CF370C31DDDC483E81DBC65C85FB8E7C8ACB237AB4D58459CA8CD1DE77871D7B', '', 1, 2, 'TMS HSM RSA Key', 1, 'System', CURRENT_TIMESTAMP());
INSERT INTO tbl_keys (data, check_digit, key_cryptographic_type_id, key_usage_type_id, description, status, created_by, created_on) VALUES ('952392F5CB403F240719CB199CFDC511', '1DDD47', 2, 2, 'NIBSS ZMK 3DES Key', 1, 'System', CURRENT_TIMESTAMP());
INSERT INTO tbl_keys (data, check_digit, key_cryptographic_type_id, key_usage_type_id, description, status, created_by, created_on) VALUES ('952392F5CB403F240719CB199CFDC511', '1DDD47', 2, 2, 'NIBSS BDK 3DES Key', 1, 'System', CURRENT_TIMESTAMP());
INSERT INTO tbl_keys (data, check_digit, key_cryptographic_type_id, key_usage_type_id, description, status, created_by, created_on) VALUES ('AAB9188201281F3780DC7915EDD413C1', '7FF5B8CDB7E70000', 2, 2, 'TMS ZMK 3DES Key', 1, 'System', CURRENT_TIMESTAMP());
INSERT INTO tbl_keys (data, value_under_parent, check_digit, key_cryptographic_type_id, key_usage_type_id, description, status, created_by, created_on) VALUES ('64E5DA90FC0E0A413A255779A5F3D253', '64AFC94F77D81F6E777E088F0D06CF00', '5A44FE', 2, 4, 'PAN Encryption 3DES Key', 1, 'System', CURRENT_TIMESTAMP());
INSERT INTO tbl_keys (data, value_under_parent, check_digit, key_cryptographic_type_id, key_usage_type_id, description, status, created_by, created_on) VALUES ('2739F92068222AF3AE2675D143E85E83', '', 'D2DBF5', 2, 4, 'PAN Hash Salt Key', 1, 'System', CURRENT_TIMESTAMP());


INSERT INTO tbl_key_maps (code, key_id, created_by, created_on) VALUES ('TMS_HSM_PUBLIC_KEY', 1, 'System', CURRENT_TIMESTAMP());
INSERT INTO tbl_key_maps (code, key_id, created_by, created_on) VALUES ('CTMS_ZMK', 2, 'System', CURRENT_TIMESTAMP());
INSERT INTO tbl_key_maps (code, key_id, created_by, created_on) VALUES ('CTMS_BDK', 3, 'System', CURRENT_TIMESTAMP());
INSERT INTO tbl_key_maps (code, key_id, created_by, created_on) VALUES ('TMS_ZMK', 4, 'System', CURRENT_TIMESTAMP());
INSERT INTO tbl_key_maps (code, key_id, created_by, created_on) VALUES ('PAN_ENCRYPT', 5, 'System', CURRENT_TIMESTAMP());
INSERT INTO tbl_key_maps (code, key_id, created_by, created_on) VALUES ('PAN_HASH', 6, 'System', CURRENT_TIMESTAMP());

-- Merchants (test purpose)
INSERT INTO tbl_merchants (name, code, address, phone_no, domain_id, enable_all_tran_type, enable_all_bin, description, status, created_by, created_on) VALUES ('Test Merchant 1', 'TESTMERCHANT001', 'Merchant Address', '08055475028', 3, 1, 0, 'Test Merchant 1', 1, 'System', CURRENT_TIMESTAMP());
INSERT INTO tbl_merchants (name, code, address, phone_no, domain_id, enable_all_tran_type, enable_all_bin, description, status, created_by, created_on) VALUES ('Test Merchant 2', 'TESTMERCHANT002', 'Merchant Address', '08055475029', 3, 1, 0, 'Test Merchant 2', 1, 'System', CURRENT_TIMESTAMP());

-- Terminals (test purpose)
-- 2039661C, 20390059
INSERT INTO tbl_terminals (code, serial_no, manufacturer, model_no, build_no, os, os_version, firmware_no, description, status, created_on, created_by) VALUES ('20390059', 'P352701711018088', 'WPOS', 'WPOS-3', 'WPOS-3_V1.01_18020715_userdebug', 'Android', '5.1.1', 'SP_V1.21_B0T0_170920', 'WPOS Terminal', 1, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_terminals (code, serial_no, manufacturer, model_no, build_no, os, os_version, firmware_no, description, status, created_on, created_by) VALUES ('2039661C', 'P327701708002831', 'WPOS', 'WPOS-3', 'WPOS-3_P2_V00.02_170810_android_international', 'Android', '5.1.1', 'SP_V1.21_B0T0_170920', 'WPOS Terminal', 1, CURRENT_TIMESTAMP(), 'System');

-- Terminal Parameters (test purpose)
INSERT INTO tbl_terminal_parameters (name, tms_endpoint_id, ctmk_key_id, bdk_key_id, key_download_time_in_min, key_download_interval_in_min, terminal_type, terminal_capabilities, terminal_extra_capabilities, transaction_currency, transaction_reference_currency, transaction_currency_exponent, reference_currency_exponent, reference_currency_conversion, force_online, pos_data_code, icc_data, support_default_tdol, support_default_ddol, support_pse_selection, use_local_network_config, description, status, created_on, created_by) VALUES ('Default', 1, 2, 2, 1440, 60, 22, 'E090C8', '7F80C0F0FF', '0566', '0566', '2', '2', '0', true, '510101511344101', '9F26,9F27,9F10,9F37,9F36,95,9A,9C,9F02,5F2A,82,9F1A,9F34,9F33,9F35,9F03', true, true, true, true, 'Default Terminal Parameter Download group', 1, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_terminal_parameters (name, tms_endpoint_id, ctmk_key_id, bdk_key_id, key_download_time_in_min, key_download_interval_in_min, terminal_type, terminal_capabilities, terminal_extra_capabilities, transaction_currency, transaction_reference_currency, transaction_currency_exponent, reference_currency_exponent, reference_currency_conversion, force_online, pos_data_code, icc_data, support_default_tdol, support_default_ddol, support_pse_selection, use_local_network_config, description, status, created_on, created_by) VALUES ('TermParam1', 2, 2, 2, 1440, 60, 22, 'E090C8', '7F80C0F0FF', '0566', '0566', '2', '2', '0', true, '510101511344101', '9F26,9F27,9F10,9F37,9F36,95,9A,9C,9F02,5F2A,82,9F1A,9F34,9F33,9F35,9F03', true, true, true, true, 'Default Terminal Parameter Download group', 1, CURRENT_TIMESTAMP(), 'System');

-- Endpoints (test purpose)
INSERT INTO tbl_endpoints (hostname, ip, port, timeout, ssl_enabled, description, status, created_by, created_on) VALUES ('ctms.nibss-plc.com', '196.6.103.72', 5042, 60, false, 'NIBSS CTMS', 1, 'System', CURRENT_TIMESTAMP());

-- HSM Endpoints (test purpose)
INSERT INTO tbl_hsm_endpoints (ip, port, timeout, ssl_enabled, description, status, created_by, created_on) VALUES ('localhost', 1501, 60, false, 'Simulator HSM', 1, 'System', CURRENT_TIMESTAMP());

-- Setup Active HSM
INSERT INTO tbl_active_hsms (code, hsm_endpoint_id, created_by, created_on) VALUES ('TMS_HSM', 1, 'System', CURRENT_TIMESTAMP());


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
INSERT INTO tbl_acquirers_terminal_parameters (acquirer_id, term_param_id, created_on, created_by) VALUES (2, 2, CURRENT_TIMESTAMP(), 'System');

-- Merchant Terminal Parameters (test purpose)
INSERT INTO tbl_merchants_terminal_parameters (merchant_id, term_param_id, created_on, created_by) VALUES (1, 2, CURRENT_TIMESTAMP(), 'System');

-- Terminal Terminal Parameters (test purpose)
INSERT INTO tbl_terminals_terminal_parameters (terminal_id, term_param_id, created_on, created_by) VALUES (1, 2, CURRENT_TIMESTAMP(), 'System');
INSERT INTO tbl_terminals_terminal_parameters (terminal_id, term_param_id, created_on, created_by) VALUES (2, 2, CURRENT_TIMESTAMP(), 'System');





