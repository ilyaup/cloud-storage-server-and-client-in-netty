To run server and client, you should run "main" method in cloudserver package in CloudServer class. After it is loaded <br/>
and running, you should run "main" method in cloudclient package in CloudClient class. When it is running, you can run
these commands:

login &lt;login&gt; &lt;password&gt; <br/>
Example: <br/>
login Ilya 1234 <br/>
<br/>
download &lt;file_path&gt; &lt;dir_path&gt; <br/>
First argument is a path to file on a server, this string must not end with a file separator. <br/>
Second argument is a path to directory on a client side, this string must end with a file separator. <br/>
Example: <br/>
download C:\Users\ilyau\Desktop\serverFiles\funny.jpg C:\Users\ilyau\Desktop\clientFiles\
<br/>
upload &lt;dir_path&gt; &lt;file_path&gt; <br/>
First argument is a path to directory on a server, this string must end with a file separator. <br/>
Second argument is a path to file on a client side, this string must not end with a file separator. <br/>
Example: <br/>
upload C:\Users\ilyau\Desktop\serverFiles\ C:\Users\ilyau\Desktop\clientFiles\funny.jpg <br />
<br/>
move &lt;file_path&gt; &lt;dir_path&gt; <br/>
First argument is a path to file, this string must not end with a file separator. <br/>
Second argument is a path to directory, this string must end with a file separator. <br/>
Example: <br/>
move C:\Users\ilyau\Desktop\serverFiles\nice\minecraft_1.12.2.zip C:\Users\ilyau\Desktop\serverFiles\clay\ <br/>
<br/>
copy &lt;file_path&gt; &lt;dir_path&gt; <br/>
First argument is a path to file, this string must not end with a file separator. <br/>
Second argument is a path to directory, this string must end with a file separator. <br/>
Example: <br/>
copy C:\Users\ilyau\Desktop\serverFiles\nice\funny.jpg C:\Users\ilyau\Desktop\serverFiles\clay\ <br/>
<br/>
All examples are on Windows. On UNIX systems and Linux should work as well with their own file separators. 
