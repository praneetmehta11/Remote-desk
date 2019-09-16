package com.remotedesk.networking.server;
public class QuitStatus
{
private boolean quit;
QuitStatus()
{
quit=false;
}
public void setQuit()
{
this.quit=true;
}
public boolean isQuit()
{
return this.quit;
}
}