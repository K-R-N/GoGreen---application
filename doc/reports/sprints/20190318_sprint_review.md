# Spint review week 3.5

## Main positivity encountered
#### Positivity 1: Better time management; a lot was done on friday already

#### Positivity 2: Better discussion and better task division on merge requests

#### Positivity 3: Connected a lot on the GUI to the database

## Main problems encountered
#### Problem 1: There was still some miscommunication about how/when one should approve merge requests
We discussed this more thoroughly on the meeting, so this is clear for everyone now.

#### Problem 2: There were a lot of merge conflicts due to big merge requests which were changing the same classes
To prevent this from happening again, we'll create a subbranch for development which will be very up to date,
 in trade off that it won't always be fully tested.  
We'll also try to create smaller merge requests.

#### Problem 3: Communication wasn't perfect yet and it wasn't always clear what everyone worked on
To mitigate this, we'll do a daily standup on slack where everyone tells what they did that day.

## Individual progress and blocking issues
* **Jules van der Toorn**  
    * worked on the new sidebar
    * made a lot of progress early in the week
    * created the 'add activity' window on sunday, thought that it wouldn't take too long
    * took quite long and caused a lot of stress on sunday evening --> won't take new big tasks last second in the future
    
* **Rami Al-Obaidi**  
    * started late because of linear algebra midterm
    * could've done more --> will contribute more in the next sprint
    * had to make important schema changes
    * had it done on saturday
    
* **Kevin Nanhekhan**  
    * worked on implementing friends page
    * was done on friday
    * ported it over to the new sidebar
    * looked at merge requests of server
    * had issues with connecting CSS to View
    * learned more about how the server worked
    
* **Yuxin Jiang**  
    * had to implement server side methods
    * had to wait on rami to change db things
    * then tried on sunday, but was too late --> will get more isolated tasks in the next sprint
    * when, he worked worked well

* **Jonathan van Oudheusden**  
    * listed all needed methods on the server
    * worked on connecting client to server
    * problems with testing his code due too less mock data
    * was dependent on server for connecting last things, so was done late too
    * had issues with testing

* **Aleks Bako** 
    * was done by wednesday
    * worked on hashing and autocomplete
    * looked into authentication
    * helped yuxin with debugging and his issues

* **Dimitar Petrov**  
    * didn't do much, so didn't had any issues
    * implemented profile page
    * learned a lot from the View class
    * didn't start working on the 'add activity' window --> will communicate dropping a task better in slack in the future
    * had to study hard this weekend so couldn't do a lot
    
## Adjustments from previous sprints  
* enforce deadline on saturday night for reviewing/merging all open merge requests
> was useful for the merge requests that made this deadline, although some merge requests we're still open on sunday.  
We'll enforce this deadline more in the next sprint.
* tweak the workload per issue better (don't create too large issues)
> this went really well this sprint and wasn't an issue anymore.
* keep better track of the time during meetings, so we don't run out of time
> Aleks kept track of the time and we were done in time! We'll keep trying to do this.

## Adjustments for next sprint
* create a development subbranch
* always first merge master into own branch before creating merge request
* do daily standups on slack to keep updated of each others their progress