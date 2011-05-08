package org.nlogo.hubnet.client

import org.nlogo.swing.Implicits._
import org.nlogo.window.ClientAppInterface
import java.awt.BorderLayout
import java.awt.event.{WindowAdapter, WindowEvent}
import javax.swing.{WindowConstants, JFrame}
import org.nlogo.swing.{ModalProgressTask, OptionDialog}
import org.nlogo.awt.Utils
import org.nlogo.hubnet.connection.Ports
import org.nlogo.api.{I18N, CompilerServices}

/**
 * The HubNet client. 
 **/
object ClientApp {
  private var localClientIndex = 0

  // called by App.main()
  def mainHelper(args: Array[String], editorFactory: EditorFactory, workspace: CompilerServices) {
    try {
      val app = new ClientApp()
      if (System.getProperty("os.name").startsWith("Mac"))
        net.roydesign.mac.MRJAdapter.addQuitApplicationListener(() => app.handleQuit())
      org.nlogo.swing.Utils.setSystemLookAndFeel()

      var isRoboClient = false
      var waitTime = 500
      var userid = ""
      var hostip = ""
      var port = Ports.DEFAULT_PORT_NUMBER

      for (i <- 0 until args.length) {
        if (args(i).equalsIgnoreCase("--robo")) {
          isRoboClient = true
          if (i + 1 < args.length) {
            try waitTime = args(i + 1).toLong.toInt
            catch {
              // it is not the optional wait time parameter
              case nfe: NumberFormatException => org.nlogo.util.Exceptions.ignore(nfe)
            }
          }
        }
        else if (args(i).equalsIgnoreCase("--id")) userid = args(i + 1)
        else if (args(i).equalsIgnoreCase("--ip")) hostip = args(i + 1)
        else if (args(i).equalsIgnoreCase("--port")) port = (i + 1).toInt
      }
      app.startup(editorFactory, userid, hostip, port, false, isRoboClient, waitTime, workspace)
    } catch {
      case ex: RuntimeException => org.nlogo.util.Exceptions.handle(ex)
    }
  }
}

class ClientApp extends JFrame("HubNet") with ErrorHandler with ClientAppInterface {
  import ClientApp.localClientIndex

  private var clientPanel: ClientPanel = _
  private var loginDialog: LoginDialog = _
  private var isLocal: Boolean = _

  locally {
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
    setResizable(false)
  }

  def startup(editorFactory: org.nlogo.window.EditorFactory, userid: String, hostip: String,
              port: Int, isLocal: Boolean, isRobo: Boolean, waitTime: Long, workspace: CompilerServices) {
    Utils.invokeLater(() => {
      Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
        def uncaughtException(t: Thread, e: Throwable) {
          org.nlogo.util.Exceptions.handle(e)
        }
      })

      this.isLocal = isLocal
      setIconImage(Utils.loadImageResource("/images/arrowhead.gif"))
      getContentPane.setLayout(new BorderLayout())
      loginDialog = new LoginDialog(this, userid, hostip, port, true)
      clientPanel =
        if (isRobo) new RoboClientPanel(editorFactory, this, waitTime, workspace)
        else new ClientPanel(editorFactory, this, workspace)

      getContentPane.add(clientPanel, BorderLayout.CENTER)
      pack()
      Utils.center(this, null)

      if (isLocal) {
        val killLocalListener = () => {
          clientPanel.logout()
          ClientApp.this.dispose()
        }

        addWindowListener(killLocalListener)
        loginDialog.addWindowListener(killLocalListener)

        // increment first, otherwise if two local clients are
        // started in rapid succession they might collide
        // ev 7/30/08
        localClientIndex += 1
        login("Local " + localClientIndex, hostip, port)
      }
      else {
        addWindowListener(() => handleExit())
        Utils.center(loginDialog, null)
        loginDialog.addWindowListener(() => handleQuit())
        doLogin()
      }
    })
  }

  private def doLogin() {
    /// arggh.  isn't there some way around keeping this flag??
    /// grumble. ev 7/29/08
    if (!isLocal) {
      dispose()
      loginDialog.doLogin()
      login(loginDialog.getUserName, loginDialog.getServer, loginDialog.getPort)
    }
  }

  def completeLogin() {setVisible(true)}

  private def login(userid: String, hostip: String, port: Int) {
    val exs = Array[String](null)
    new ModalProgressTask(Utils.getFrame(this), () => exs(0) = clientPanel.login(userid, hostip, port), "Entering...")
    if (exs(0) != null) {
      handleLoginFailure(exs(0))
      clientPanel.disconnect(exs(0).toString)
    }
  }

  def showExitMessage(title: String, message: String): Boolean = {
    Utils.mustBeEventDispatchThread()
    val buttons = Array[Object](title, I18N.gui.get("common.buttons.cancel"))
    0 == org.nlogo.swing.OptionDialog.show(loginDialog, "Confirm " + title, message, buttons)
  }

  def handleDisconnect(activityName: String, connected: Boolean, reason:String) {
    Utils.mustBeEventDispatchThread()
    if (isLocal) this.dispose()
    else {
      if (connected) Utils.invokeLater(() => {
        OptionDialog.show(this, "", "You have been disconnected from " + activityName + ".", Array("ok"))
        ()
      })
      doLogin()
    }
  }

  def handleLoginFailure(errorMessage: String) {
    Utils.mustBeEventDispatchThread()
    OptionDialog.show(loginDialog, "Login Failed", errorMessage, Array(I18N.gui.get("common.buttons.ok")))
  }

  def handleExit() {
    Utils.mustBeEventDispatchThread()
    if (showExitMessage(I18N.gui.get("common.buttons.exit"), "Do you really want to exit this activity?"))
      clientPanel.logout()
  }

  def handleQuit() {
    Utils.mustBeEventDispatchThread()
    if (showExitMessage(I18N.gui.get("common.buttons.quit"), "Do you really want to quit HubNet?")) destroy()
  }

  def destroy() {
    Utils.invokeLater(() => System.exit(0))
  }
}
