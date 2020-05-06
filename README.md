WishList

Is a simple application based on android api and running in Java + XML as android studio is designed for.

This application is a systeme based on only one device with heavy client data base, the systeme of the data base is whole relational table, no json and no recursive table allowed in this project.

The request in the database have to be done in SQLite and not SQL, SQLite is simpler but not complete enough from my point of view.

Application:

User sign in, mail based primary key, Bcrypt hashed password (not for the moment but at the final release)

User sign up required infos : compulsory -> mail, pseudo, password
                              optionals -> photo, address
                              
This application provides users to create WishLists which contain several product choose by the user who wishes to receives theim.

Users can create WishList, private public or cooperation edition mode.
Users can add product in a WishList he owns or in a friend's WishList wich allow him writting permission ( cooperation edition mode)
Users can add others users as friend. The requested user have to accept the proposition before the requester user can view the requested user's WishList.

Users can delete a friend.. (obviously)
