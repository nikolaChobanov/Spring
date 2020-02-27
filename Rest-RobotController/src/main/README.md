Robots are placed on a hypothetical infinite grid with a set of coordinates and a direction.

Robots are only allowed to move in northern or eastern direction. If pointing to south or west two aditional links are returned 

First one to cancel the movement and delete the robot 

Second one to correct the robot positioning by pointing it at a correct cardinal point.

Robot movements are set with a letter string e.g. "RAALAL" which means

  - Turn right

  - Advance twice

  - Turn left

  - Advance once
  
  - Turn left
  
  Data is saved to MongoDB
  
  Logs are saved to Logs.log file
