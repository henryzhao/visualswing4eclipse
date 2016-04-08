# What is it? #
<img src='http://visualswing4eclipse.googlecode.com/svn/trunk/org.dyno.visual.swing.feature/logo.png' />Visual Swing for Eclipse is a GUI designer tool, which consists of a set of Eclipse Plug-ins. It aims to provide a Swing designer for Eclipse for Java desktop developers.
# Update site #
http://visualswing4eclipse.googlecode.com/svn/trunk/org.dyno.visual.swing.site/
# Requirement #
  * Eclipse 3.3 or higher
  * Java 5 or higher
# Introduction #
It is well known that Swing is gaining more and more momentum in recent years. Before Java 1.5, Swing was ugly, slow and full of bugs. In fact, it used to be so bad, that IBM created another GUI toolkit named SWT, on which Eclipse was built. SWT was a great success. Sun felt the pressure from Eclipse. Since Java 1.5, Sun has been improving Swing. The Swing team made a lot of progress and Swing is winning back its lost ground. Actually, it won the No. 1 in GUI toolkit market three years ago. At that time, there was a market research report given by Evans Data Corporation that Swing is the dominant GUI Toolkit for Northern American developers. The following statement is quoted from one of Hans Muller's blog. It was excerpted from the report:

> "Java Swing with 47% use, has surpassed WinForms? as the dominant GUI development                                          toolkit, an increase of 27% since fall 2004."

(http://weblogs.java.net/blog/hansmuller/archive/2005/10/official_swing.html)

As a Swing lover, I am very happy to witness the progress on Swing in recent years. The latest good news about Swing is the newly released Java 6 update 10, namely the consumer JRE. If you don't know what Java 6 update 10 is and what it has do with Swing, please take a look at the following page:

http://java.sun.com/developer/technicalArticles/javase/java6u10

For a long time, GUI development in Java is very weak. One of the reasons is that Swing lacks good GUI designers. The Matisse Project in NetBeans brought hopes to swing developers. But we are very sad to notice that although Eclipse dominates the Java IDE market, but it lacks a good open source designer. Although it used to have one, ie. VE, it was painful to use. Now this project seems to be abandoned by eclipse. Of course there are some commercial tools in the market. But they are either not free or not open sourced. Developers using Eclipse for free have to manually code swing applications.

This project is determined to produce a free, open-sourced and Netbeans-like GUI designer for the Eclipse IDE. It now has the following features:

  * Designing Swing component like NetBeans visual designer, especially the component layout, providing similar anchors, baselines and layout hints etc.

  * Generating and parsing java code without the help of .form file. The code generated is readable, like those of VE(visual editor of eclipse).

  * Look and feel support during design, preview and the generated code.

  * Visual tree and table designing. It is very intuitive to design tree and table in this tool. What you need is only drag and drop a table/tree, and invoke the in-place editor to add or delete rows or columns or editing data in the table, etc.

  * Provide visual menu designer. This is not a structural designing like those of other designers. You can drag and drop menu items directly on to the form. The look and feel is exactly what you get.

  * Responsive and fast. This toolkit uses the same JVM with eclipse. And code generation follows editing-save-editing cycle. Therefore it is very responsive and fast.

NOTE that the latest version requires Eclipse 3.4 or Eclipse 3.4.1. The java runtime environment requires to be Java 6. If you want to test Nimbus look and feel, you should install java 6 update 10.

This project is released under EPL(Eclipse Public License 1.0) so that everyone can freely get the source code and make contributions back.

Why not just download and have a try? Please don't hesitate to tell me your feedback.