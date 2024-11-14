
INSERT INTO account (id, created_at, first_name, last_name) VALUES
('a48f0ea0-2ce2-4def-87ba-54fe0904b807', '2024-11-14 10:02:19.487', 'Wojtek', 'Malek');

INSERT INTO wallet (id, created_at, amount, wallet_currency, account_id) VALUES
('1b6382b2-ba1e-4a36-8f2d-351aa1e1c3ee', '2024-11-14 10:15:26.815', 1000, 'PLN', 'a48f0ea0-2ce2-4def-87ba-54fe0904b807'),
('aef7aa47-bbd1-448d-8f0e-c654de3b0d29', '2024-11-14 10:15:26.815', 200, 'USD', 'a48f0ea0-2ce2-4def-87ba-54fe0904b807');