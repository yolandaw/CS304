create table borrower
(borr_bid integer not null PRIMARY KEY,
borr_password varchar(30) not null,
borr_name varchar(30) not null,
borr_address varchar(50),
borr_phone integer,
borr_emailAddress varchar(50),
borr_sinOrStNo integer not null,
borr_expiryDate date,
borr_type varchar(20));

create table borrowerType
(bt_type varchar(20) not null PRIMARY KEY, 
bt_bookTimeLimit date);

create table book
(book_callNo integer not null PRIMARY KEY,
book_isbn integer not null,
book_title varchar(50),
book_mainAuthor varchar(30),
book_publisher varchar(50),
book_year integer);

create table hasAuthor
(book_callNo integer not null,
hasAuthor_name varchar(30) not null,
PRIMARY KEY(book_callNo, hasAuthor_name),
foreign key (book_callNo) references book);

create table hasSubject
(book_callNo integer not null,
hasSubject_subject varchar(30) not null,
PRIMARY KEY(book_callNo, hasSubject_subject),
foreign key (book_callNo) references book);

create table bookCopy
(book_callNo integer not null,
bookCopy_copyNo integer not null,
bookCopy_status integer,
PRIMARY KEY(book_callNo, bookCopy_copyNo),
foreign key (book_callNo) references book);

create table holdRequest
(holdRequest_hid integer not null PRIMARY KEY,
borr_bid integer not null,
book_callNo integer,
holdRequest_issueDate date,
foreign key (borr_bid) references borrower,
foreign key(book_callNo) references book);

create table borrowing
(borrowing_borid integer not null PRIMARY KEY,
borr_bid integer not null,
book_callNo integer not null,
bookCopy_copyNo integer,
borrowing_outDate date,
borrowing_inDate date,
foreign key (borr_bid) references borrower,
foreign key (book_callNo, bookCopy_copyNo) references bookCopy);

create table fine
(fine_fid integer not null PRIMARY KEY,
fine_amount float,
fine_issueDate date,
fine_paidDate date,
borrowing_borid integer not null,
foreign key (borrowing_borid) references borrowing);
