delete from order_item;
delete from "order";
delete from order_status;
delete from payment_status;

INSERT INTO payment_status ("id", "name") VALUES
(0,	'None'),
(1,	'Billed'),
(2,	'Paid'),
(3,	'Canceled');

INSERT INTO order_status ("id", "name") VALUES
(0,	'New'),
(1,	'Pending'),
(2,	'Delivery'),
(3,	'Closed'),
(4,	'Canceled');

INSERT INTO "order" ("id",
                      "customer_id",
                      "items_count",
                      "price_total",
                      "payment_status",
                      "order_status",
                      "creation_time") VALUES
(1,	207,	3,	333.33,	1,	1,	'2019-02-26 18:15:40.942000'),
(2,	1,	3,	555.55,	2,	2,	'2019-02-26 19:13:10.172000'),
(3,	1,	3,	999.99,	1,	1,	'2019-02-26 19:14:40.908000'),
(4,	207,	2,	666.66,	2,	2,	'2019-02-26 15:59:01.371000'),
(5,	207,	1,	333.33,	3,	4,	'2019-02-26 16:33:52.335000');

INSERT INTO order_item ("order_id",
                      "id",
                      "name",
                      "description",
                      "category",
                      "price",
                      "offer_id") VALUES
(1,	1, 'TestOffer1',	'TestDescription1', 'cat1',	111.11,	1),
(1,	2, 'TestOffer1',	'TestDescription1', 'cat1',	111.11,	1),
(1,	3, 'TestOffer1',	'TestDescription1', 'cat1',	111.11,	1),
(2,	4, 'TestOffer1',	'TestDescription1',	 'cat1',	111.11,	1),
(2,	5, 'TestOffer2',	'TestDescription2',	 'cat2',	222.22,	2),
(2,	6, 'TestOffer2',	'TestDescription2',	 'cat2',	222.22,	2),
(3,	7, 'TestOffer2',	'TestDescription2',	 'cat2',	222.22,	2),
(3,	8, 'TestOffer2',	'TestDescription2',	 'cat2',	222.22,	2),
(3,	9, 'TestOffer3',	'TestDescription3',	 'cat3',	333.33,	3),
(4,	10,	'TestOffer3', 'TestDescription3', 'cat3',	333.33,	3),
(4,	11,	'TestOffer3', 'TestDescription3', 'cat3',	333.33,	3),
(5,	12,	'TestOffer3', 'TestDescription3', 'cat3',	333.33,	3);




