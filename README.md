The program is developed on the basis of Telegram-API and the Backend framework of the Java programming language - Spring Boot. 
So the entire user interface is already presented in the messenger itself. When starting to work with the bot, the user sends him the /start command, 
which is responded to by one of the processors, and sends him a reply with an indication of interaction with the bot.
After that, the user can share their location. The bot responds to this with the handleLocation handler, passes its coordinates, 
they are passed to the following methods, and starts searching for the nearest shelter among those in the maps.json file. 
The result of this will be an answer, information about the nearest hiding place, the user will receive a district, and an address with the accuracy of the house.

![image](https://github.com/castromx/Near_shelter_SpringBot/assets/96194271/d1e1710a-8753-4e2f-a251-f8fe3285d059)

During the course work, I got acquainted with one of the most common Backend frameworks in Java - Spring, and learned to interact with the Telegram-API using the Telegrambots library in Java. Learned to interact with .json files on Java 17 version, and with the dependency manager - maven. Familiarized with Hash Maps and 'Overridden' methods. Developed a Telegram bot for finding the nearest shelter in the city. Developed handlers of various types and events, and increased my level of programming in Java.
