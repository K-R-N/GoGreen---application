# Spint review week 3.3

## Main positivity encountered
### Positivity 1: Eventually we connected a lot of components

### Positivity 2: The was good discussion in merge requests

### Positivity 3: The GUI progress went well

### Positivity 4: The motivation for work was good

## Main problems encountered
### Problem 1: Tasks were too vague  
Not everyone understood their task correctly, which resulted in not getting everything done from the sprint backlog.  
The obvious solution which we'll use from now on is to be clearer about the expectations of an issue.

### Problem 2: Not everyone got enough tasks
Some people actually had too few tasks and didn't know what to work on.  
To mitigate this, we discussed that you should speak up if you think you can't do enough work.  
Ofcourse, we also will try to improve on dividing tasks.

### Problem 3: The technical communication was too opaque
To try to improve on this point we will:  
* say in the Slack that what you'll be working on
* share new technical findings to your team members
* create the data models before tuesday evening, so the team can build on those models.  

### Problem 4: Time management was not good enough
We decided that code + tests should be done before friday night, so there'll be enough time for anticipation and code reviewing.

### Problem 5: Commits were too infrequent and too big
To mitigate this, we'll try to make it a habit to commit more with fewer changes.

## Individual progress and blocking issues
* **Jules van der Toorn**  
    * got a lot of errors trying to upgrade to JDK 11
    * had blocking issues when trying to combine all seperate branches into working release
    * did what he wanted to do  
    
* **Rami Al-Obaidi**  
    * had issues trying to get JDK 11 to work
    * created JDBC class with comments, which went well
    * had blocking issue whilst trying to test JDBC code, will work on this in next sprint
    
* **Kevin Nanhekhan**  
    * was sick during the week
    * still made sketches and basic implementation of homescreen
    * encountered blocking issue relating switching scenes in GUI
    
* **Yuxin Jiang**  
    * started working on the server but had problems understanding how data exchange is implemented
    * then tried figuring out JDBC, but stopped when Rami did that part already
    * tried to do things but couldn't do much due to miscommunication

* **Jonathan van Oudheusden**  
    * didn't really got any tasks  
    * reviewed a lot of merge requests
    * had blocking issue when trying to connect to the database
    * eventually worked on getting POST on server and making blueprint code for session management

* **Aleks Bako**  
    * had a lot of issues trying to upgrade project to JDK 11
    * also had blocking issues trying to figure out sessions
    * did learn a lot about authentication
    * created modeling classes

* **Dimitar Petrov**  
    * had issues with JDK 11 and upgrading IntelliJ to ultimate
    * found out he didn't know a lot about project structure
    * worked on homescreen together with Kevin
    
## Adjustments from previous sprints
* remove branches in GitLab after they get merged
> worked well for the project overview, will continue to do this

* upload the agenda no later than saturday evening
> was immediately useful, as points from other team members were added to it

## Adjustments for next sprint
* write clearer titles of issues
* make smaller commits and more frequently
* set deadline for finishing task + tests on friday night
* set deadline for creating model classes on tuesday night
* speak up if you think there's something wrong with your tasks