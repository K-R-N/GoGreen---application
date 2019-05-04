# Final Report

## General

   First of all, we would like to say we accomplished our task and stuck to our plan perfectly. Our team always had a plan
   ahead of the schedule, for example, we always set our own deadline for the demo one week before the actual deadline. Even 
   though sometimes we still had something to modify after our own deadline, the good planning protected us from running 
   into a rush and messed things up. Besides, we had a timeline for each week as well, In case that someone's work is 
   dependent on others', we set sub deadlines for ourselves in one week, Tuesday is the deadline for model change, Friday 
   is the deadline for task accomplishment, Saturday is the deadline for closing all the merge request to master, the 
   model and database changes always happened at the start of the week, these sub deadlines made the workflow went 
   smoothly because blocking issues were reduced, everyone was clear about when he could start his work and when he should
   finish. Even though we made detailed plans, an unexpected model change still happened during the project, in that week, 
   the whole team worked whole nights to finish corresponding changes on client side and server side and successfully made
   the deadline, which became a memorable experience for us. Last but not least, I will say sprint review also helped us 
   stick to our plan, constant reflection on how we act kept the whole team on track.
   
   The collaboration in our team also went very well. At first, we had client group, server group, GUI group and database
   group, different groups worked separately in first week. From the second week, the need for connection between different 
   groups increased dramatically, some team members started to work in different groups to connect everything. As the case in
   most groups, some of our group members are more experienced in programming, after their work was done, they were very willing
   to help other team members in their work, and not just by completing others'task, instead, they offered some guidance 
   on what could be a better way to do the job, recommended some good online tutorials etc, which really helped other group 
   members understand the knowledge. Another point needs to be mentioned is that we share knowledge frequently, if a 
   member find some good link relevant to the part he worked on, he will post it on the group chat and make everybody 
   could benefit from it.  
   
   As to our group communication, at the very beginning, we chose whatsapp as our platform, after the first week, we 
   switched to slack, which separates different channels, making everyone could easily see the most relevant conversation 
   for them. Another issue we came up during the project is that we need to keep updated about others' progress, in order 
   to achieve that, we created standups channel in slack, all team members wrote in that channel daily about what 
   he did the day before and what he planned to do the day after, it turned out to be a very useful tool especially for those 
   whose works are related. Another thing could not be ignored is our group meeting, every one of them was quite efficient,
   we had a plan beforehand, everybody contributed to the meeting and spoke out their opinions, making the meeting a fun
   experience for all of us.
  
   We used git as our version control tool, it helped to make our project more organized. After the first week's practice, 
   we got used to this tool quickly. However, as the project went on, more and more merge conflicts occurred, the way we 
   come up with to solve this problem is every time we want to merge branch into master, we merge master into our own 
   branch first and then solve the merge conflicts in our own branch, when everything is set, we merge our own branch
   back into master, in this way, the master is well protected and the whole project looks more neat. Another idea we came
   up with to make group work more organized is using development branch. For every week, we created a development branch at the 
   beginning, we branched out from that development branch and merged our own branch back after we were done, at the end of the week, we 
   merged development branch into master. The reason we chose to do that is we could all merge our progress together without
   it being ready to be merged into master (because they couldn't be tested yet), in this way, we are able to have code 
   that is reliant on each other in the same branch as soon as possible. At the same time, since we could merge our code
   together sooner, we would have less big merge conflicts.
   
   In conclusion, we learned a lot from this project. Firstly, everyone got a better knowledge on how a complete
   java project should be structured, how to connect every parts to make a functioning program. Besides, our programming 
   capability also improved significantly, especially in the area we were in charge of. In case that the majority of this 
   project's material is not covered in the lecture, we needed to search a lot of information by ourselves, which helped  
   us to know better how to find out what we need quickly online. Last but not least, for most of us, it is the first 
   time to work as a team in a programming project, we learned a lot on how to cooperate with others, how to solve
   conflicts in the team, for example, we noticed the importance to keep updated about other team members' progress, we 
   also understood that the procedure of the project needs to be well discussed before anything happened. Overall, this 
   project was definitely a valuable lesson for our team and we benefited from it a lot.
   

## Design Decisions

* ### Major decisions as a team
   
   The first non-technical choice we made together as a team was we dividing our team into several different groups, client 
   group, server group, GUI group and database group, generally, same team members worked for the same group during the whole project.
   This way of division guaranteed that we are professional in our own area. However, we assigned people from other groups
   to check the merge requests, which make everyone have a overall view of the whole project.
   
   The next decision we made is the model we have, for example, we have classes for user, activity, achievement and friendrequest
   etc. Those are the basis for the whole project, we discussed and modified them several times during the project.
   
   Another decision we made together is regarded to the different kinds of activities and achievements in our app, including 
   how many green points we should have for each activities. 
   
* ### Technical choices we made

   We chose to use JavaFx because it is consistent across components, it is easy to write modular, clean and maintainable 
   code using JavaFx. We also chose pure JavaFx code instead of using Scene builder because it is easier to implement and 
   connect later to other components compared to others. 

   Choosing to use the Java Spring Framework[[1](https://spring.io/projects/spring-framework)] was a major decision made early in the project. The decision was made by evaluating
   the three major points when starting a project, stability, efficiency and privacy. For stability it was accepted after we saw
   that most of large companies on the market used the Spring Framework. For efficiency we discovered that it had all the libraries
   that the average developer needs for creating a standard application,and by sticking with it we avoided trying to connect different types of frameworks together, which would have been more prone to error.
   The Spring Boot library is the prime example as to why choosing this particular framework was extremely efficient.
   By writing a few lines of code the team already had a basic server with very little functionality, which boosted the teams moral as well. 
   As for privacy, the Spring libraries contains most of the tools a person needs to mold a very secure application, with libraries
   such as BCryptpasswordencoder[[2](https://docs.spring.io/spring-security/site/docs/4.2.11.RELEASE/apidocs/org/springframework/security/crypto/bcrypt/BCryptPasswordEncoder.html)],
   which is very important for making sure that our users feel secure when working on our application.
  
   The decision to use REST API [[3](https://en.wikipedia.org/wiki/Representational_state_transfer)] was largely influenced by our decision to use the Java Spring Framework. The Spring framework
   does contain a lot of different libraries that help set the a developer on the right path of developing the desired application.
   The REST API just like the Spring Framework, supports a large variety of things and provides all the tools to deal with any issue that may come across.
   REST API unlike most other API's does not have a standard which makes it more flexible and easy to use for developers with not a lot of experience.
   
   For testing the REST based server controller methods we chose to use MockMvc instead of mockito, because MockMvc is 
   specifically made create mock Model view controllers. MockMvc is the perfect tool for testing controller methods, as the application evolves
   and becomes more complex you need to be prepared to use a tool specifically made for the job,
    otherwise your ability to do things will be limited and problems will arise.
   
   We chose to use SQLite for testing because it is a light database and it will help us test the queries which could not be 
   possible if use something like Mockito. Since it is a file database it will work with the CI. Besides, we went with 
   JDBC over JPA because we could have more control on the database and it is easier to test and debug. JDBC also has 
   a better performance over JPA. At last, we chose Postgres because we are already familiar with this tool and we need a fully featured RDMS for our server. we also
   designed the database schema to reduce data redundancy and make writing queries more efficient and make the database 
   scalable.

   PostgreSQL was an important asset for the development for your application. It is released under an open source licence[4] and meets most of modern SQL standards. 
   It has modern features that we believe we will help us develop a robust backend for our project such as modern JDBC drivers, multiple specialized data types,
   concurrency controls, point in time recovery, robust authentication, procedural language support(PL/PGSQL), etc[5]. It is also the relational database management system 
   that all of the team members have experience with. We chose to use SQLite for testing because it is an excellent embedded database 
   and can be used not just to test the Java JDBC methods, but also the SQL queries that these Java methods contain. 
   In an earlier stage of development, we used DBUnit, it is a JUnit extension for testing database-driven project but we faced an issue with the coverage report due to a 
   bug within Jaccoo, we decided to switch to testing with an embedded database. The choice of JDBC over JPA framework was its improved performance and the direct
   communication with the database. JPA would have also caused issues with testing for the team since it is harder to debug. 
   A drawback of JDBC is the maintainability of the code since SQL queries have to be stored in Java classes. We remedied this problem by storing the larger queries
   in an xml file so they are retrieved during runtime.The databse schema has been designed with care.
   All the proposed changes to the database schema are discussed during the weekly meeting, then an ER diagram and 
   relational schema diagram are uploaded to our GitLab repository to be reviewed by the team before the changes are made to ensure maintanability. 

  ## Points for improvement

   In total, everyone is really happy with our final product. The application looks really well and functions as 
   expected. We think that we even have done more than we had expected, but there are still some small things we could 
   improve. 
   
   Firstly, in the "Add activity" window, we could change the current format of adding an activity to
   a simple slide button, which will have all the different ranges of numbers and depending on where you put your
   cursor (depending of the length of the activity) you will get different amount of points. Because currently we have
   too many activities with the same name and only the last part of each activity in a group is different.
    
   Another thing we could possibly add is an option to receive a new password on your entered e-mail address, 
   if the user has forgotten his current password.The email address would be used for resetting 
   the password: the email would give the user a link to a page where he could put in his new password, and then, 
   from that moment on, he could use that new password to login. This way the e-mail field in the registration will
   be of use. Finally if we had a little more time, we could also make this program available for android users. This 
   way users can use it whereever they are and add their activities as soon as they complete them.      

## Individual feedback
### **Dimitar Petrov**

* #### Reflection

   To start with, for me this entire project was an amazing and very unique experience. I had a lot of fun during the last
   two months and I'm really grateful that I had the opportunity to work with such motivated and inspired people.
   Everybody turned out to be a very nice person and I'm happy that this led to an awesome and positive atmosphere during
   every single one of the meetings. The whole course for me felt like something I would do for fun, rather than something
   that is mandatory and I just have to do.
 
* #### Personal development
  ##### introduction
    
   So, in the beginning I was a little nervous and lost, because I've never done such a thing and didn't know what exactly
   I should do and how the entire process will go. But as time passed, with every single week I began to get used to the 
   environment and things slowly began to click. During our second meeting everybody had to choose on which part they
   wanted to work on the project and I chose to be in charge of the GUI along side with Kevin and Jules. I had never
   programmed in JavaFX before, but I wanted to learn it, because I was tired of seeing results of my work only in the 
   ugly terminal. This decision turned out to be great, because I got what I wanted and managed to design the whole
   visual application. 
   
  ##### weak points
 
   During the learning process, I encountered a lot of issues, regarding not only JavaFX, but also Git. Luckily all of
   my team members were really helpful. Every time I encountered a blocking issue and got stuck, they would immediately
   try to figure out a way to solve my problem and always help me with it. Also, at the beginning, I had mentioned that 
   I struggle to motivate myself sometimes and actually do my task. However this was not an issue during this OOP project,
   first of all, because I really enjoyed the process in total and secondly, because all of the people from the group
   were very serious and dedicated to their work and that also pushed me to do my part as well. My biggest weak point,
   however, is that sometimes parts of my work remained not fully finished and people had to compensate for me.
   
  ##### strong points
   
   As I said before, I worked on the GUI for the application; that's where my main contribution is. I created the layout
   for most of the pages and designed them. Apart from that I connected some of them to the actual data and actually
   made them work. During the meetings I often came up with various of ideas for the project itself: improving
   it, adding a new feature, changing the design style, etc. 
   In summary I managed to reach my goals for this project: I learned a lot of new things, gained some experience as in
   working as a team, found out how a real life application actually works and managed to work on what I like. Also, I met
   with some awesome people and saw their point of view for the project. I always listened to them and tried to speak
   last during the meetings, so that I heard everybody's opinion and took different perspectives into consideration.
   
### **Jonathan van Oudheusden**
   
* #### Reflection
   
   This was a very good and fun project, I learned a lot during this quarter, including how to work better with a team, 
   and how to make an application. I am happy to have a good group where everyone did there best. 
    
* #### Personal development
  ##### introduction
       
   For the application I started with working on the server part. Making the basic methods and just learning how it would work. 
   At first, the server was very simple, but after we got to know it better how it worked, it got better. So I worked together with a couple other people on the server of the application.
   I figured out how sessions work in spring and how to make them work in our client, so how to send and receive a cookie in the client.
   And because of this, I also worked on the methods to get data from and to the client. 
   When more of the essential server methods were done, I focused more on the client and helped others to work on the server.
   
  ##### weak points
    
   Sometime during this project when something had to be done, I would work alone to get it done without asking the help of team mates, 
   which are were very capable to help me. This would make it that I had to spend more time that I probably don't have to to get it done.
   
   I improved on this quite a lot, when I have something that I don't have/want the time for,
   I asked someone if they can help me, by either telling me how something works or working on some part of it. 
   When I started on this project I did not have any programming experience outside of what I learned in OOP, 
   so almost everything was new to me. I did not know how to do what needed to be done in this project. 
   I worked hard to get to understand how things work, like spring or other code team members wrote. 
   And now I know how spring works and how all parts of the project work. 
      
  ##### strong points
      
   Like I said in my personal development plan, I was/am very interested to learn everything needed in making a working app. 
   I worked hard to get my things done on time, especially when someone is dependent on my work. 
   when problems came up I would spend a lot of effort to make it work. There were times when this would mean working
   after midnight and one time all night.
   Because I wanted to learn how everything works I know how everything in the server and the client-server connection works, 
   and a lot of how the connection between the server-database and the gui works.
   
   We had a lot of problems that would come up during the project. Like very big merge requests that would be almost 
   impossible to fix for one example. But every week week during the meeting we would come up with solution to the problems.
 
   ### **Jules van der Toorn**
   
*  #### Reflection
   This was the first real project of the CSE bachelor. I think it's great to get group work for a course, but not too much ;).  
   On the negative side I think working in a group isn't really suited for the university, as that's mostly targeted at 
   self development and that you don't get that much concrete knowledge from it.  
   On the positive side though, these are the things I went to college for. I always worked alone and would never learn 
   how to properly work together without projects like these. I learned so much from it, mainly that you can really efficiently divide sub tasks to people that are better suited for them and that you, if you respect each others' work, really can get done a lot in a short amount of time.  
   In the introduction text that we all had to upload the first week I stated that I like to take leadership in projects. This also reflected in this project, as I really liked to handle the task division and keeping the project on track in general. I learned better what I do like to take leadership in and where I less like it.
   Overall, I learned a lot from the project and really enjoyed the whole workflow around it.
   
*  #### Personal development
   ##### Introduction
   I worked mainly on the GUI, the visual aspect of the project. Developing the visual aspects of an application really isn't my favorite part though. The reason that I took a lot of responsibility in this part is mainly because I did have the most experience on it, because previously I developed a full fledged application on iOS.  
   Because my coding strength is mainly backend, I worked a lot on developing a great code base to easily make buttons, popups, scrolling etc. for the other people on the frontend to use that. I did really learn how to better structure frontend code from that.
   
   ##### Weaker points
   My weakest point in the project was definitely taking too much responsibility for the team. I've remodeled the core Models of the project just before I was going away for the weekend because I thought that it had to be done. In hindsight this was just really stupid of myself. When you work alone you can do these kind of things, but for groupwork you either have to plan ahead better or thoroughly discuss changes like these.
   Other than weak points groupwise, I also took too much responsibility for myself. I've spent way more time per week on coding than anticipated, just because I thought I could make some things even better. I find it hard to deliver half work, and I was also afraid of the project not being finished on time if I didn't do what I did. I found it hard to improve on this point, as I also really liked to work on the project. The main problem with this was me getting behind schedule of other courses, so it's really a personal thing. I'll keep trying to better get my priorities straight ;).
   
   ##### Stronger points 
   My strongest point during the project was me taking the lead. I've helped a lot in the beginning to get all the important structural things in place, like writing guides on how to create the weekly reports. I also kept track on the rubric to see which points we could get and which points we still needed to get. I also worked on general things to improve the workflow, like implementing Continuous Integration on GitLab and always positively approaching people on Slack or during the meetings. I tried to learn a lot on Scrum and agile development and immediately tried to apply it to the weekly meeting to see what worked and what didn't.  
   Due to this search for improvement I also got better at dividing tasks and recognizing the strong and weak points of people.
   Codewise I think I helped a lot on the visual aspect and could help a lot of team members when they got stuck on their task.                                            

### **Kevin Nanhekhan**

* #### Reflection

    The project was a good learning experience. Everything was structured, there was good communication between team members 
and everyone did his part of the workload. Most people of our group even went as far as getting no sleep to get things done. 
And that is also one of the things I like about this group, that everyone does his part seriously. 
 
* #### Personal development
  ##### introduction
    
   I only worked on the GUI, so I did not do any coding on the server/database/client side of things. 
But despite me doing only GUI side of coding, I think I did a reasonably good job together with Dimitar and Jules who also 
worked on it. This is because everything looks like how we as a team had discussed it. Even though I did not know a lot of 
doing GUI coding before the project, I'm happy I did that part. This is because of the satisfying feeling I got after seeing 
something I partly designed myself looking like a real life application. 
   
  ##### weak points
 
   There were some unexpected problems during the project such as a lot of tests failing where I as someone who works on the GUI 
   could not help much with because I had the thought that I would make things worse if I tried to help since I did not know how 
   the testing exactly was done. But despite this I tried to help as much as I could for example for merge request look at 
   the code and see if it works as intended. I also looked if it made sense for as much as I could comprehend it. 
   
  ##### strong points
   
   Even though I’m not much of a talker as said in my personal development plan, I tried to join in on the conversations, 
   even though it wasn’t much talking from my side. I also tried to learn from the other for example learning the usage of views, 
   how server side is sort of done and such. Even though I did not have much experience with programming, I know I could always 
   depend on the others in case I was stuck on something. This also applies for the other way around, in case the others had 
   questions on things I understood, I tried to explain it in the best way I could.
   
  ###   **Rami Al-Obaidi**
   
* #### Reflection
   I worked on the backend side of the project specializing in the database department. I was responsible in creating 
   and maintaining the databases that we used for our application (PostgreSQL) and for testing (SQLite). I worked on
    making the server be able to send and receive data from the database and optimize the methods/queries work in efficient ways.
   
* #### Personal development
   ##### introduction
   
   Me and my team were able to communicate efficiently. We always stayed in touch when were away and had open discussions 
   to solve any conflicts. Our planning early in the week enabled us to meet deadlines on time.
   
  ##### weak points
   
   My weak points were mainly to do with my weakness in networking as I wasn’t able to help with working on
   client-server communication. I feel this is a part where I need to improve on later as it is very critical
   in application development. I also felt that I lacked in contributing in other departments of the project,
   but I compensated this by perfecting the database department.
    
  ##### strong points
     
   My strong points was mainly related to my good background in databases and my passion for data related topics.
   I was able to contribute to my team with helping in ensuring that we have a stable and efficient database 
   and guide them on testing database related methods in the project.

###   **Aleks Bako**

* #### Reflection

   The project was an amazing and unique experience.
   The joy of working on one thing with a group of people to reach a common goal was great,
   and I think that there was no better group of people to work with than the one I had the pleasure of working with.
   From day one we made the whole project structured in such a way that it gave me a sort of vibe that
   we might be actually doing this for a living, which was fun.
   Our plan made it so there were no major setbacks and that made it less stressful.
   There were no conflicts throughout the whole project.
   The support that we provided for each other made the whole project a very enjoyable learning experience.
   Having situations when we all of us stayed up all night to fix errors was exhausting, but it made
   for a very fun experience, I noticed that by working together we became an unstoppable machine of productivity,
   which gave me more motivation to code no matter the consequences, the consequences being the lack of sleep.


* #### Personal development
  ##### introduction

  For the application I started off working on the server side, 
because I wanted to implement all the functionality that the 
frontend would take advantage of to show the user the capabilities of my code.
I worked with different people, because some were switching their working positions, whilst i stayed on the server part of the project
 with the responsibility that everything on the server would be functional and up to date.
I had a lot of help from other people when things became messy on the server side of things
and I relied a lot on the database part, which made me 
realize how insignificant the whole product is if any of the parts were missing.

  ##### weak points

  I would say that the biggest weakness for me throughout this project 
was my lack of curiosity. I had specific ideas for a basic application
and I searched for just a simple solution to solve the problems that were needed to be solved,
not even looking if there was an even more efficient way of dealing with it.
Without other peoples interference and ideas this project would have been half or maybe even less good
than it is right now. I did want to write complex lines of code, but that would have ended up being inefficient 
 and could have affected the team negatively therefore I don't really feel bad about not doing it. 
  
  ##### strong points
  
  One of my strongest points throughout the whole project was the control of the server
and the awareness that if I were to not do my part then there would be a lot of issues
that would follow in other parts of the project. I would say that the other strongest point would be
the wish to have the whole project structured, I just felt like without having
all of the tasks structured and approached one by one everything would have collapsed,
that's why we all agreed on doing daily stand-ups,  to keep each other updated of what we did the day
before and if any of us were stuck on something. 
I needed the support of people and I'm sure at times they needed my support, 
which is why having everything clear and structured plan 
made asking for help a guilt free activity
All the research on server methods and security helped my way of looking at this whole project.
  
### **Yuxin Jiang**

* #### Reflection

   In general, this project was a very pleasant experience, I can’t imagine a better team I could meet, sometimes I was 
   totally impressed by my team member’s fantastic work, which pushed me to work harder and keep up. Everyone is so 
   helpful and very easy to communicate to, making the whole process went smoothly.
    
* #### Personal development

  ##### introduction
    
   I would like to say I learned a lot from this project, It is my first time to work with others as a team to make a 
   programming project, I was mainly in charge of server side, which is a brand-new area for me, I searched a lot of 
   information on how the server side code should be structured and definitely grasped lots of new knowledge. Even
   though some of my work could not be done by me completely, I still learned a lot from looking into my team member’s 
   code.
      
  ##### weak points
 
   As for my weak points, I will say from this project, I find that I still have difficulty solving a problem I 
   encounter by myself,  the reason why I couldn’t do that is I kind of can’t connect the different parts 
   of knowledge I learned before, like OOP and web. When I got stuck on some point, I got a little bit impatient and not
   confident about my ability. Secondly, I think my strategy to search information online still need to be 
   improved, at the beginning of the project, I spent quite a lot time on looking for the right information I need, but 
   it is getting better as the project moves on, I could find information resource quickly now.

   ##### strong points
 
   As for my stronger points, Firstly, I will say I know better for myself how a complete java project should look like,
   how the different parts as GUI, client, server and database should combine together to make the whole program work, 
   which I think could be one of the most valuable lessons I learned from this project. Besides, my collaboration ability
   has also improved, I learned how to solve the conflicts between me and my team members and delivered the best result 
   together. 

                                                    
## Value Sensitive Design

Our application is designed for those people who are willing to make an individual influence on the carbon footprint of
the entire world. Everybody who has regular access to a computer and internet can benefit from this application. He/She
can make a small change in the global scale and also have fun doing it. We added the function of adding friends and checking
their progress as well. This means you don't have to be alone in your journey and seeing how others also try to make a
difference in the world, can motivate the user.<br/>

   In general we seek to motivate the users and create an endless ripple. Because the majority of the activities in the 
app are something that don't need much effort and can be done on a regular basis. In this way the user only need
only 1 simple action, start using the app and soon he will make a progress in life. He will start feeling
better for himself, knowing he is doing something positive and as time goes on, he tend to do more and more environmental-friendly
activities. Also if this app would go on the market, we would definitely make a lot more major changes, so that the 
user does not get bored and has some more choices and maybe even "cooler" design updates.<br/>

   To make our major improvements, we would contact many social scientists first. We want to see first the user's 
general opinion on the environment and how he/she can contribute even by little; what can make them start caring more
for our nature. We will make a lot of studies on the people from all kinds of ages and see whether they like our
idea and maybe even suggest some improvements.<br/>

   After that we could collaborate with ecologists and scientists who study the environment. From them we can learn more
about which changes are most valuable and most needed. They will give us a professional opinion on our idea and
gives us new ways and ideas to tackle this global problem.<br/>

   Together with both type of scientists we can really make a big difference, not only influence a small amount of
people but also make a difference in the world.<br/>

   Our main goal after consulting those specialists in the field, receiving feedback from potential clients and 
creating the final design for the app, will be to spread the idea as much as possible. So for that we would take into
account Android development, because the amount of people who use their mobile phones on a regular basis is way much
than those who use a computer or even own one. That's why we would also adjust our application to be suitable for android
users. This way they could download it on their phones and use it where ever and when ever they want. The only thing
they'll need is access to the internet, which is not hard to find in our modern society.<br/>

   In conclusion this idea, if taken seriously and if it were helped by some specialist in the field, could really
make an impact to our society. Everything is being modernised and with the advance of technology people forget what is
happening outside of the digital world. That's why they'll need a reminder: our application. Which we will make them
more cautious about the changes in the environment and mainly carbon footprint. After that they can start and take
action when ever they can, nevertheless, even the contribution of 1 single person will make a progress.

    