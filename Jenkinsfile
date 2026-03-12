//Declarative Groovy script
pipeline //Pipeline keyword
{
	agent any //run the build whenever agent available in pipeline
	
	tools {
		maven 'maven - 3.9.13'
	}
	
	stages {
		stage('Checkout') {
			steps {
				git branch: 'main', url: 'https://github.com/AbhilashPrabhakaran9/Selenium-Automation-Framework.git'
			}
		}
		
		stage('Build'){
			steps {
				bat 'mvn clean install' //Clean the project and install the maven artifacts
				//bat- batch command for windows and for linux,macOS we are using sh
			}
		}
		
		stage('Test') {
			steps {
				bat 'mvn test'
			}
		}
		
		stage('Reports') {
			steps {
				publishHTML(target: [
					reportDir: 'src/test/resources/ExtentReport',
					reportFile: 'ExtentReport.html',
					reportName: 'Extent Spark Report' //I can give any name
					
				])
			}
		}
	}
	
	//post build for the UI purpose to showcase the report on jenkins UI artifacts and here you can see it will show the 
	//reports once build is done then it shows the report in jenkins UI
	post 
	{
		always {
			archiveArtifacts artifacts: '**/src/test/resources/ExtentReport/*.html',fingerprint: true
			junit 'target/surefire-reports/*.xml'
		}
		
		//Once post build, if success then i am mentioning down the email format
		success 
		{
			emailtext (
				to: 'abhilash.9prabhakaran@gmail.com',
				subject: "Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
				body: """
				<html>
				<body>
				<p>Hello Team,</p>
				<p>The latest Jenkins build has completed.</p>
				
				<p><b>Project Name:</b> ${env.JOB_NAME}</p>
				<p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
				<p><b>Build Status:</b> <span style="color: green;"><b>SUCCESS</b></span></p>
				<p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
				
				<p><b>Last Commit:</b></p>
				<p>${env.GIT_COMMIT}</p>
				<p><b>Branch:</b> ${env.GIT_BRANCH}</p>
				
				<p><b>Build log is attached.</b></p>
				
				<p><b>Extent Report:</b> <a href="http://localhost:8080/job/OrangeHRM_Test/HTML_20Extent_20Report/">Click here</a></p>
				
				<p>Best regards,</p>
				<p><b>Automation Team</b></p>
				</body>
				</html>
				""",
				mimeType: 'text/html',
				attachLog: true
				
				)
				
				}
				
				failure {
					
					emailtext (
				to: 'abhilash.9prabhakaran@gmail.com',
				subject: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
				body: """
				<html>
				<body>
				<p>Hello Team,</p>
				<p>The latest Jenkins build has <b style="color: red;">FAILED</b>.</p>
				
				<p><b>Project Name:</b> ${env.JOB_NAME}</p>
				<p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
				<p><b>Build Status:</b> <span style="color: red;"><b>FAILED #10060;</b></span></p>
				<p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
				
				<p><b>Last Commit:</b></p>
				<p>${env.GIT_COMMIT}</p>
				<p><b>Branch:</b> ${env.GIT_BRANCH}</p>
				
				<p><b>Build log is attached.</b></p>
				<p><b>Please check the logs and take necessary actions.</b></p>
				
				
				<p><b>Extent Report(if available):</b> <a href="http://localhost:8080/job/OrangeHRM_Test/HTML_20Extent_20Report/">Click here</a></p>
				
				<p>Best regards,</p>
				<p><b>Automation Team</b></p>
				</body>
				</html>
				""",
				mimeType: 'text/html',
				attachLog: true
				
				)
					
				}
				
				
				
	}
}












