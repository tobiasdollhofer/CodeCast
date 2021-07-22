# CodeCast
Plugin for the IntelliJ platform designed to provide spoken explanations for source code. Bachelor thesis project of Tobias Dollhofer (University of Regensburg 2021).

## Requirements
- IntelliJ/Android Studio with 2021.x Version
- de.tobiasdollhofer.CodeCastFragebogen, used for logging and sends log files to server
- com.intellij.javafx, used for mp3 playback
- Http-Server to store audio files
- Ftp-Server to store log files

## Used Libraries
- IntelliJ Plugin SDK
- JavaFX
- Apache Commons IO
- JAudiotagger
- OpenCSV

## How it works
Audio-Files have to be provided via webserver (URL must end with file extension, eg. https://server.com/file.mp3). When opening a project providing codecast-comments (see below) files from the server will be downloaded to the user home directory. You can play the comments using the Player UI on the right side of the IDE. There is also the ability to start and pause playback directly in the editor window as well as jump to code position where the explanation belongs to.

## CodeCast comments
To provide audio comments for a project you have to add block comments with a specific syntax shown below:
```java
/*
@codecast 1. ChapterTitle | 1. CommentTitle (type <intro/default>)
@url https://www.server.com/project/01-01-Intro.mp3
*/
```
All comments with the same ChapterTitle will get in the same chapter ordered by its leading number.
You can also add a general information shown below the player by adding following block comment:
```java
/*
@codecast-info Here you can find information about this project and
who has created it and so on.
 */
```


This plugin is designed and tested for IntelliJ and Android Studio. 