INSERT INTO tbl_merchants_terminals (merchant_id, terminal_id, created_on, created_by) VALUES (1, 2, CURRENT_TIMESTAMP(), 'System');

INSERT INTO tbl_endpoints (hostname, ip, port, timeout, ssl_enabled, description, status, created_by, created_on) VALUES ('ctms.nibss-plc.com', '196.6.103.72', 5043, 60, true, 'NIBSS CTMS SSL', 1, 'System', CURRENT_TIMESTAMP());

