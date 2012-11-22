create table borrower
(borr_bid integer not null PRIMARY KEY,
borr_password varchar(30),
borr_name varchar(30),
borr_address varchar(50),
borr_phone integer,
borr_emailAddress varchar(50),
borr_sinOrStNo integer,
borr_expiryDate date
bt_type varchar(20)
foreign_key (bt_type) references borrowerType;

create table borrowerType
(bt_type varchar(20), 
bt_bookTimeLimit integer);

create table book
(book_callNo integer not null PRIMARY KEY,
book_isbn integer not null,
book_title varchar(50),
book_mainAuthor varchar(30),
book_publisher varchar(30),
book_year integer);

create table hasAuthor
(book_callNo integer not NULL,
hasAuthor_name varchar(30)
foreign key (book_callNo) references book);

create table hasSubject
(book_callNo integer not null,
hasSubject_subject varchar(30)
foreign key (book_callNo) references book);

create table bookCopy
(book_callNo integer not null,
bookCopy_copyNo integer,
bookCopy_status varchar(20)
foreign key (book_callNo) references book);

create table holdRequest
(holdRequest_hid integer not null PRIMARY KEY,
borr_bid integer not NULL,
book_callNo integer,
holdRequest_issueDate date
foreign key (borr_bid) references borrower
foreign key(book_callNo) references book);

create table borrowing
(borrowing_borid integer not null PRIMARY KEY,
borr_bid integer not null,
book_callNo integer not null,
bookCopy_copyNo integer,
borrowing_outDate date,
borrowing_inDate date
foreign key (borr_bid) references borrower
foreign key (book_callNo) references book
foreign key (bookCopy_copyNo) references bookCopy);

create table fine
(fine_fid integer not null PRIMARY KEY,
fine_amount float,
fine_issueDate date,
fine_paidDate date,
borrowing_borid integer not null
foreign key (borrowing_borid) references borrowing);

create sequence hid_counter;