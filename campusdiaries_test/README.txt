This is sample signup process automation for Campus Diaries (test).

Code consists of two source packages - 
1. Login_Process
2. Parsing Body_Header

1. Login_Process

That consists fo two tests = signup and then mail verification

Technology Stack - TestNG,Selenium WebDriver
Code has two more features - it will generate log file and it will take screenshot if any of the method fails.


2. Parsing Body_Header

Technology Stack - Java, Jsch library,curl in linux
Code will execute curl command in linux machine via making an SSH connection and write the output of that command to a file.
That file will then be downloaded to test machine and a small code will parse that file to see and validate few thing.
Like - Request is authorized or not, if body is having anu error or not, or the respons error code is >400 or not, etc.

Few sample files are also included for this example, which can be parsed by giving them as input file to the "Parse.java".

To build the project - Just open eclipse -> Import Projects -> Give the path to this repo(wherever it is downloaded)
Then Set build path -> right click on project and then set build path -> Three dependencies which are already there in project in "lib" folder. Just point it to those files.

While running make sure to give proper arguments(inputs in input file). 
To run - 
1. Login_Process - Just run "SignUpAndValidate.java" (Right click and run as TestNG suite)
	Or else - right click on testng.xml and run.
	
2. Parsing Body and Header - Just run "Parse.java".
	(Give credentials of linux system in input file 'input.properties').
	