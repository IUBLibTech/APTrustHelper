APTrustHelper is a small Java GUI application which assists with transmitting files exported from a DSpace repository to APTrust.

Prerequisites
=============
Before using the program, you'll need the following.
* The bagit-java executable, downloaded from https://github.com/LibraryOfCongress/bagit-java.
* The AP Trust helper tools (Windows only), downloaded from https://sites.google.com/a/aptrust.org/aptrust-wiki/home/partner-tools.
* A valid configuration file which contains the credentials needed to connect to AP Trust. See https://sites.google.com/a/aptrust.org/aptrust-wiki/home/partner-tools for more details on how the config file should be constructed.

How To Use
==========
* Click the aptrusthelper_clickable.jar file to start the application.
* Set up the locations of your bagit-java executable, AP Trust tools, and configuration file using the buttons on the GUI. You'll need to set these locations up only once unless they change.
* Use the "Show Receiving Bucket" button to list the current files in your receiving bucket.
* If you want to upload a DSpace item, first download it from DSpace. Select the downloaded zip file, choose a bag name, and prefix, and click the 'Upload' button.

What Upload Does
================
1. Unzips the DSpace zip file.
2. Uses bagit-java to create a bag of the zip file contents.
3. Name the bag using the institutional prefix and handle from the DSpace handle file.
4. Add the aptrust-info.txt file to the bag.
5. Tar the bag.
5. Validate the bag using the AP Trust tools.
6. If the bag passes validation, upload the bag using the AP Trust tools.

Current Issues and Limitations
==============
1. When uploading bags, the program is currently throwing an error about getting an MD5 receipt. However, the file does upload successfully.
2. Eventually the bagit-java code could be integrated rather than used as a separate program.
3. Works with Windows only.
4. Works only with single-item zip files exported directly from a DSpace 4 or 5 repository.
5. Currently bag name is specified each time. Might be possible to pull bag name from package metadata.
