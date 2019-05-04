# Spint review week 3.2
This sprint was the first full sprint of the team. We created our first release which was a demo.  
In general, the sprint went really well. Ofcourse there were some problems, but no major ones for this sprint.

## Main problems encountered
### Problem 1: Too much branches  
After one full week of tasks, there are a lot of merged branches in our repository.
This resulted in us losing sight of the project tree.
To counter this, we will remove branches after they get merged.
> to do this automatically, tick 'Delete source branch when merge request is accepted' in your GitLab merge request.

### Problem 2: Some issues with git  
After learning git in the first week, there were still some uncertainties about creating branches from an unmerged branch.
To solve this, we discussed during the meeting how this should be done.

### Problem 3: No time to add own entries to agenda
Because we decided to upload our meeting agenda on the evening before the meeting, there was no time for other team members to add their own points of discussion to it.  
To counter this, we'll upload the agenda no later than saturday evening from now on.

## Individual progress and blocking issues
* **Jules van der Toorn**  
    Worked on the web api for the client, which went fine.
    A blocking issue was creating tests for the methods of it, which are dependent on the internet connection and on the build-in HTTP library from Java.
    This was solved by using PowerMock to create mock objects for testing.
    
* **Rami Al-Obaidi**  
    Created ER-diagram and schema of database. Also did research about database triggers and implemented one.  
    Did research about how to communicate with the database via Java.
    Figured out how to use JDBC but got a blocking issue whilst trying to get JPA to work.  
    After discussion on the meeting we decided to use JDBC for simplicity.
    
* **Kevin Nanhekhan**  
    Learned how to use JavaFX and created a login screen, went well.
    
* **Yuxin Jiang**  
    Did research about spring, and watched tutorials about it.
    Looked into Spring, Spring MVC and Spring Boot during the process to see the differences.  
    Created an interface for communicating with the database later on.
    Had no blocking issues except for the learning curve of Spring.

* **Jonathan van Oudheusden**  
    Did research about the server but noticed that others already finished it in the meantime (divided tasks better for next sprint based on this).  
    Then did some further research about creating tests on the server and returning JSON data to the client, which went well.

* **Aleks Bako**  
    Set up the core of the server in one sitting, which worked good for him.  
    A blocking issue was trying to return a JSX template to the client. This wouldn't work because of a 'circular view' error.  
    After discussion on the group chat and the meeting we concluded that we won't need templates anyway (only plaintext JSON), so this issue could be discarded.

* **Dimitar Petrov**  
    Watched videos about JavaFX and looked at code other people wrote to get a better grasp of how it works.  
    He learned a lot and didn't have any blocking issues.
    
## Adjustments from previous sprints
* take a break each hour during discussion
> This really helped a lot and resulted in us being more productive and calm. We will continue to do this for the project.
* book a better suited meeting room
> We booked a room, where we sat during the whole meeting. This made it easier to discuss as group because we could see and hear everyone at the same time.  
Another advantage was the white board in the room, which was very useful for creating sketches of the GUI.

## Adjustments for next sprint
* remove branches in GitLab after they get merged
* upload the agenda no later than saturday evening