//  LINGUAGGIO DI PROGRAMMAZIONE CALVINO
//  Copyright (C) 2015 Tommaso Anzidei
//
//  LICENZA (GPL:
//  This program is free software; you can redistribute it and/or
//  modify it under the terms of the GNU General Public License
//  as published by the Free Software Foundation; either version 2
//  of the License, or (at your option) any later version.
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
//  See the GNU General Public License for more details.
//  You should have received a copy of the GNU General Public License
//  along with this program; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//
//  QUESTO SW CONTIENE I BINARI DI ALCUNE APPLICAZIONI DELLE QUALI RIPORTIAMO DI SEGUITO LA LICENZA:
//
//  PROCESSING:
//  We use GPL v2 for the parts of the project that we've developed ourselves.
//  For the 'core' library, it's LGPL, for everything else, it's GPL.
//  Over the course of many years of development, files being moved around,
//  and other code being added and removed, the license information has become
//  quite a mess. Please help us clean this up so that others are properly 
//  credited and licenses are consistently/correctly noted:
//  https://github.com/processing/processing/issues/224
//  (SEGUE LA LICENZA GPL, CFR. https://github.com/processing/processing/blob/master/license.txt)
//
//  SYMJA:
//  the underlying libraries (Symja core libraries) are published under the LESSER GNU GENERAL PUBLIC LICENSE
//  the parser (and simple numeric evaluators) are published under the APACHE LICENSE Version 2.0.
//  the Apache Commons Mathematics Library is published under Apache software licence
//  the JAS Java Algebra System is published under the (LESSER) GNU GENERAL PUBLIC LICENSE licence
//
//  JAVA TURTLE PACKAGE:
//  Copyright 2002 Regula Hoefer-Isenegger
//  This file is part of The Java Turtle Package
//  The Java Turtle Package is free software; you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation; either version 2 of the License, or
//  (at your option) any later version.
//  The Java Turtle Package is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//  You should have received a copy of the GNU General Public License
//  along with The Java Turtle Package; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package org.calvino.lang

//imports needed by AST
import static org.codehaus.groovy.syntax.Types.*
import org.codehaus.groovy.ast.stmt.*
import org.codehaus.groovy.runtime.MethodClosure
import java.lang.reflect.Method

//imports needed by AWT/Swing
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

//imports needed by Processing
import processing.core.PApplet;

//imports needed by DSL/Shell
import org.codehaus.groovy.control.CompilerConfiguration
import jline.Terminal
import jline.History
import org.codehaus.groovy.tools.shell.util.MessageSource
import org.codehaus.groovy.tools.shell.util.XmlCommandRegistrar
import org.codehaus.groovy.runtime.StackTraceUtils
import org.codehaus.groovy.tools.shell.util.Preferences
import org.fusesource.jansi.AnsiRenderer
import org.fusesource.jansi.Ansi
import org.fusesource.jansi.AnsiConsole
import org.codehaus.groovy.tools.shell.*
import org.codehaus.groovy.tools.shell.util.HelpFormatter
import org.codehaus.groovy.tools.shell.util.Logger
import org.codehaus.groovy.tools.shell.util.MessageSource
import org.codehaus.groovy.tools.shell.util.NoExitSecurityManager
import java.util.concurrent.Callable
import org.fusesource.jansi.Ansi
import org.fusesource.jansi.AnsiConsole

//imports needed by Symja
import static org.matheclipse.core.expression.F.*;
import org.matheclipse.core.basic.Config;
import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;

//imports needed by turtle computing
import ch.aplu.turtle.*

//  CLASSE PRINCIPALE
public class Calvino
{
    public static Graphics graphics
    public static Calvinosh calvinosh
    public static Algebra algebra
    public static Logo logo
    public static Computer computer


    public static version = "1.0"
    public static released = "20150221"

    public static void main(String[] args)
    {
        
        org.codehaus.groovy.tools.shell.commands.HelpCommand.metaClass.list = { ->
            // Figure out the max command name and shortcut length dynamically
            int maxName = 0
            int maxShortcut
            for (command in registry) {
                if (command.hidden) {
                    continue
                }
                if (command.name.size() > maxName) {
                    maxName = command.name.size()
                }
                if (command.shortcut.size() > maxShortcut) {
                    maxShortcut = command.shortcut.size()
                }
            }
            io.out.println()
            io.out.println('For information about @|bold Calvino|@, visit:') // TODO: i18n
            io.out.println(' @|bold http://calvino-lang.org|@ ') // FIXME: parsing freaks out if end tok is at the last char...
            io.out.println()
            // List the commands we know about
            io.out.println('Available commands:') // TODO: i18n
            for (command in registry) {
                if (command.hidden) {
                    continue
                }
                def n = command.name.padRight(maxName, ' ')
                def s = command.shortcut.padRight(maxShortcut, ' ')
                //
                // TODO: Wrap description if needed
                //
                def d = command.description
                io.out.println(" @|bold ${n}|@ (@|bold ${s}|@) $d")
            }
            io.out.println()
            io.out.println('For help on a specific command type:') // TODO: i18n
            io.out.println(' help @|bold command|@ ')
            io.out.println()
        }
       

        graphics = new Graphics()
        algebra = new Algebra()
        logo = new Logo()
        computer = new Computer()

        Binding binding = new Binding(graphics: Calvino.graphics, algebra: Calvino.algebra, logo: Calvino.logo, color: java.awt.Color, computer: Calvino.computer, math: java.lang.Math)
        

        calvinosh = new Calvinosh(binding, new IO(System.in, System.out, System.out))
        //calvinosh = new Calvinosh(gcl, binding, new IO(System.in, System.out, System.out))

        for (int i = 0; i < 50; ++i) System.out.println();

        org.calvino.lang.Start.shell = calvinosh

        org.calvino.lang.Start.main(args)

    }
}

//  GEOMETRIA DELLA TARTARUGA
public class Logo
{
    TurtleFrame turtleFrame = new TurtleFrame("Calvino Turtles", 600,600, java.awt.Color.white)

    private isVisible = false
    
    private isStarted = false

    public Logo()
    {
        this.hide()
    }

    public Turtle addTurtle(java.awt.Color color)
    {
        return new Turtle(turtleFrame, color)
    }

    public Turtle addTurtle()
    {
        return new Turtle(turtleFrame, java.awt.Color.blue)
    }

    public hide()
    {

        turtleFrame.setVisible(false)
        isVisible = false

    }
    public show()
    {
        if(!isStarted)
        {
            isStarted=true;
            
            turtleFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            turtleFrame.addWindowListener(new java.awt.event.WindowAdapter() {
             public void windowClosing(java.awt.event.WindowEvent  e) {
                hide();
                 }
            });     
        }  

        if(!isVisible)
        {
            
            turtleFrame.setVisible(true)
            isVisible=true
        }
    }
}

// SYMJA
public class Algebra
{
    public static EvalUtilities util = new EvalUtilities(false, true);
    def compute(question)
    {
        // don't distinguish between lower- and uppercase identifiers
        Config.PARSER_USE_LOWERCASE_SYMBOLS = true;  
        return util.evaluate(question)
    }
}

// VARIE UTILITY
public class Computer
{
    
    public static rdm = new java.util.Random()

    public input(question)
    {
        def readln = javax.swing.JOptionPane.&showInputDialog
        def answer = readln(question)
        if (answer.isInteger())
            return answer.toInteger()
        if (answer.isFloat())
            return answer.toFloat()
        return answer
    }

    public random(int i)
    {
        rdm.nextInt(i)
    }

}


// QUESTO E' PROCESSING
public class Graphics extends PApplet
{
    public JFrame jFrame = new JFrame("Calvino Graphics")

    private isVisible = false
    
    private isStarted=false

    public void drawFunction()
    {

    }

    public void erase()
    {
        if(isStarted)
        {        
            size(600, 600,P3D)
            def f = {->}
            paint(f)
            background(255)
        }

    }

    public void ellipse(a,b,c,d)
    {
        super.ellipse(a.toFloat(),b.toFloat(),c.toFloat(),d.toFloat())
    }

    public void rect(a,b,c,d)
    {
        super.rect(a.toFloat(),b.toFloat(),c.toFloat(),d.toFloat())
    }   

    public void line(a,b,c,d)
    {
        super.line(a.toFloat(),b.toFloat(),c.toFloat(),d.toFloat())
    }  

    public void perspective(a,b,c,d)
    {
        super.perspective(a.toFloat(),b.toFloat(),c.toFloat(),d.toFloat())
    }  

    public void ambientLight(a,b,c)
    {
        super.ambientLight(a.toFloat(),b.toFloat(),c.toFloat())
    } 

    public void rotateX(a)
    {
        super.rotateX(a.toFloat())
    }   

    public void rotateY(a)
    {
        super.rotateY(a.toFloat())
    } 

    public float map(a,b,c,d,e)
    {
        super.map(a.toFloat(),b.toFloat(),c.toFloat(),d.toFloat(),e.toFloat())
    }    

    public pointer = this.&drawFunction

    public void setup()
    {
        size(600, 600,P3D)
        background(255,255,255);  // Setting the background to white
        stroke(0);              // Setting the outline (stroke) to black
        strokeWeight(4)         // Pen Size
        fill(255,255,255);        // Setting the interior of a shape (fill) to white
    }

    public void draw()
    {
        //super.ellipse(mouseX, mouseY, 100, 100)
        pointer()
    }



    public void hide()
    {
        //jFrame.repaint()
        //pointer = this.&drawFunction
        //setup()
        if (isVisible)
        {            
            //jFrame.setVisible(false)
            jFrame.setState(java.awt.Frame.ICONIFIED)
            isVisible = false
        }

    }

    public void clear()
    {
    }

    public void show()
    {

    	if(!isStarted)
    	{
    		isStarted=true;
		    jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		    jFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			 public void windowClosing(java.awt.event.WindowEvent  e) {
				hide();
			     }
            });
            jFrame.setVisible(true)  
            jFrame.setSize(600, 600)
            jFrame.getContentPane().add(Calvino.graphics, BorderLayout.CENTER)
            init()
            background(255,255,255)
            stroke(0)
            strokeWeight(4)
            fill(255,255,255)            
    	}


        if(!isVisible)
        {
            jFrame.setState(java.awt.Frame.NORMAL)
            isVisible=true
        }

    }
    
    public void paint(drawClosure)
    {
    	    Graphics.metaClass.drawFunction = drawClosure
    }
}

//  SHELL, IN PRATICA COINCIDE CON GROOVYSH
class Calvinosh extends Shell {

    static {
        // Install the system adapters
        AnsiConsole.systemInstall()

        // Register jline ansi detector
        Ansi.setDetector(new AnsiDetector())
    }


    final BufferManager buffers = new BufferManager()

    final Parser parser

    final CalvinoInterpreter interp
    
    final List imports = []
    
    InteractiveShellRunner runner
    
    History history

    boolean historyFull  // used as a workaround for GROOVY-2177
    String evictedLine  // remembers the command which will get evicted if history is full


    Calvinosh(ClassLoader classLoader, final Binding binding, final IO io, final Closure registrar) {
        super(io)
        
        assert classLoader
        assert binding
        assert registrar
      
        parser = new Parser()
        
        
        interp = new CalvinoInterpreter(classLoader, binding)

        registrar.call(this)
    }

    private static Closure createDefaultRegistrar() {
        return { shell ->
            def r = new XmlCommandRegistrar(shell, classLoader)
            r.register(new URL("file:///"+createCommands()))  
        }
    }
    
    private static String createCommands()
    {
    	    def ln = System.getProperty('line.separator')
    	    def fileName=System.getProperty("user.dir")+"/"+"commands.xml"
    	    def cmdFile = new File(fileName)
    	    cmdFile.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+ln)
    	    cmdFile.append("<commands>"+ln)
    	    cmdFile.append("    <command>org.codehaus.groovy.tools.shell.commands.HelpCommand</command>"+ln)
    	    cmdFile.append("    <command>org.codehaus.groovy.tools.shell.commands.ExitCommand</command>"+ln)
    	    cmdFile.append("    <command>org.codehaus.groovy.tools.shell.commands.ImportCommand</command>"+ln)
    	    cmdFile.append("    <command>org.codehaus.groovy.tools.shell.commands.DisplayCommand</command>"+ln)
    	    cmdFile.append("    <command>org.codehaus.groovy.tools.shell.commands.ClearCommand</command>"+ln)
    	    cmdFile.append("    <command>org.codehaus.groovy.tools.shell.commands.ShowCommand</command>"+ln)
    	    cmdFile.append("    <command>org.codehaus.groovy.tools.shell.commands.InspectCommand</command>"+ln)
    	    cmdFile.append("    <command>org.codehaus.groovy.tools.shell.commands.PurgeCommand</command>"+ln)
    	    cmdFile.append("    <command>org.codehaus.groovy.tools.shell.commands.EditCommand</command>"+ln)
    	    cmdFile.append("    <command>org.codehaus.groovy.tools.shell.commands.LoadCommand</command>"+ln)
    	    cmdFile.append("    <command>org.codehaus.groovy.tools.shell.commands.SaveCommand</command>"+ln)
    	    cmdFile.append("    <command>org.codehaus.groovy.tools.shell.commands.RecordCommand</command>"+ln)
    	    cmdFile.append("    <command>org.codehaus.groovy.tools.shell.commands.HistoryCommand</command>"+ln)
     	    cmdFile.append("    <command>org.codehaus.groovy.tools.shell.commands.AliasCommand</command>"+ln)
    	    cmdFile.append("    <command>org.codehaus.groovy.tools.shell.commands.SetCommand</command>"+ln)
    	    cmdFile.append("    <command>org.codehaus.groovy.tools.shell.commands.ShadowCommand</command>"+ln) 
    	    cmdFile.append("    <command>org.codehaus.groovy.tools.shell.commands.RegisterCommand</command>"+ln)
    	    cmdFile.append("</commands>"+ln)    	
    	    return fileName
    	     	    
    }

    Calvinosh(final ClassLoader classLoader, final Binding binding, final IO io) {
        this(classLoader, binding, io, createDefaultRegistrar())
    }

    Calvinosh(final Binding binding, final IO io) {

        this(Thread.currentThread().contextClassLoader, binding, io)

    }

    Calvinosh(final IO io) {
        this(new Binding(), io)
    }
    
    Calvinosh() {
        this(new IO())
    }

    //
    // Execution
    //

    /**
     * Execute a single line, where the line may be a command or Groovy code (complete or incomplete).
     */
    Object execute(final String line) {
        assert line != null
        
        // Ignore empty lines
        if (line.trim().size() == 0) {
            return null
        }

        maybeRecordInput(line)

        def result
        
        // First try normal command execution
        if (isExecutable(line)) {
            result = executeCommand(line)
            
            // For commands, only set the last result when its non-null/true
            if (result) {
                lastResult = result
            }
            
            return result
        }
        
        // Otherwise treat the line as Groovy
        def current = []
        current += buffers.current()

        // Append the line to the current buffer
        current << line

        // Attempt to parse the current buffer
        def status = parser.parse(imports + current)

        switch (status.code) {
            case ParseCode.COMPLETE:
                log.debug("Evaluating buffer...")

                if (io.verbose) {
                    displayBuffer(current)
                }

                // Evaluate the current buffer w/imports and dummy statement
                def buff = imports + [ 'true' ] + current

                lastResult = result = interp.evaluate(buff)
                buffers.clearSelected()
                break

            case ParseCode.INCOMPLETE:
                // Save the current buffer so user can build up complex multi-line code blocks
                buffers.updateSelected(current)
                break

            case ParseCode.ERROR:
                throw status.cause

            default:
                // Should never happen
                throw new Error("Invalid parse status: $status.code")
        }

        return result
    }

    protected Object executeCommand(final String line) {
        return super.execute(line)
    }

    /**
     * Display the given buffer.
     */
    private void displayBuffer(final List buffer) {
        assert buffer

        buffer.eachWithIndex { line, index ->
            def lineNum = formatLineNumber(index)
            
            io.out.println(" ${lineNum}@|bold >|@ $line")
        }
    }

    //
    // Prompt
    //

    private AnsiRenderer prompt = new AnsiRenderer()

    /*
        Builds the command prompt name in 1 of 3 ways:
           1.  Checks the groovysh.prompt property passed into groovysh script.   -Dgroovysh.prompt="hello"
           2.  Checks an environment variable called GROOVYSH_PROMPT.             export GROOVYSH_PROMPT
           3.  If no value is defined returns the default groovy shell prompt.

        The code will always assume you want the line number in the prompt.  To implement differently overhead the render
        prompt variable.
     */
    private String buildPrompt(){
       def lineNum = formatLineNumber(buffers.current().size())
       def formattedPrompt = "@|bold calvino:|@${lineNum}@|bold >|@ "

       /*
       def GROOVYSHELL_PROPERTY =  System.getProperty("groovysh.prompt")
       def GROOVYSHELL_ENV      =  System.getenv("GROOVYSH_PROMPT")

       if (GROOVYSHELL_PROPERTY)  return  "@|bold ${GROOVYSHELL_PROPERTY}:|@${lineNum}@|bold >|@ "
       if (GROOVYSHELL_ENV)       return  "@|bold ${GROOVYSHELL_ENV}:|@${lineNum}@|bold >|@ "
       */

       return formattedPrompt
    }

    public String renderPrompt() {
        //return prompt.render( buildPrompt() )
        return ">>> "
    }

    /**
     * Format the given number suitable for rendering as a line number column.
     */
    private String formatLineNumber(final int num) {
        assert num >= 0

        // Make a %03d-like string for the line number
        return num.toString().padLeft(3, '0')
    }

    //
    // User Profile Scripts
    //

    java.io.File getUserStateDirectory() {
        def userHome = new java.io.File(System.getProperty('user.home'))
        def dir = new java.io.File(userHome, '.groovy')
        return dir.canonicalFile
    }

    private void loadUserScript(final String filename) {
        assert filename
        
        def file = new java.io.File(userStateDirectory, filename)
        
        if (file.exists()) {
            def command = registry['load']

            if (command) {
                log.debug("Loading user-script: $file")

                // Disable the result hook for profile scripts
                def previousHook = resultHook
                resultHook = { result -> /* nothing */}

                try {
                    command.load(file.toURI().toURL())
                }
                finally {
                    // Restore the result hook
                    resultHook = previousHook
                }
            }
            else {
                log.error("Unable to load user-script, missing 'load' command")
            }
        }
    }

    //
    // Recording
    //

    private void maybeRecordInput(final String line) {
        def record = registry['record']

        if (record != null) {
            record.recordInput(line)
        }
    }

    private void maybeRecordResult(final Object result) {
        def record = registry['record']

        if (record != null) {
            record.recordResult(result)
        }
    }

    private void maybeRecordError(Throwable cause) {
        def record = registry['record']

        if (record != null) {
            boolean sanitize = Preferences.sanitizeStackTrace

            if (sanitize) {
                cause = StackTraceUtils.deepSanitize(cause);
            }

            record.recordError(cause)
        }
    }
    
    //
    // Hooks
    //

    final Closure defaultResultHook = { result ->
        boolean showLastResult = !io.quiet && (io.verbose || Preferences.showLastResult)
        if (showLastResult) {
            // Need to use String.valueOf() here to avoid icky exceptions causes by GString coercion
            io.out.println("${String.valueOf(result)}")
        }
    }

    Closure resultHook = defaultResultHook

    private void setLastResult(final Object result) {
        if (resultHook == null) {
            throw new IllegalStateException("Result hook is not set")
        }

        resultHook.call((Object)result)

        interp.context['_'] = result

        maybeRecordResult(result)
    }

    private Object getLastResult() {
        return interp.context['_']
    }

    final Closure defaultErrorHook = { Throwable cause ->
        assert cause != null

        io.err.println("@|bold Qfwfq! Si è verificato l'errore:|@ ${cause.class.name}:")
        io.err.println("@|bold ${cause.message}|@")

        maybeRecordError(cause)

        if (log.debug) {
            // If we have debug enabled then skip the fancy bits below
            log.debug(cause)
        }
        else {
            boolean sanitize = Preferences.sanitizeStackTrace

            // Sanitize the stack trace unless we are in verbose mode, or the user has request otherwise
            if (!io.verbose && sanitize) {
                cause = StackTraceUtils.deepSanitize(cause);
            }

            def trace = cause.stackTrace

            def buff = new StringBuffer()

            for (e in trace) {
                buff << "        @|bold at|@ ${e.className}.${e.methodName} (@|bold "

                buff << (e.nativeMethod ? 'Native Method' :
                            (e.fileName != null && e.lineNumber != -1 ? "${e.fileName}:${e.lineNumber}" :
                                (e.fileName != null ? e.fileName : 'Unknown Source')))

                buff << '|@)'

                io.err.println(buff)

                buff.setLength(0) // Reset the buffer

                // Stop the trace once we find the root of the evaluated script
                if (e.className == Interpreter.SCRIPT_FILENAME && e.methodName == 'run') {
                    io.err.println('        @|bold ...|@')
                    break
                }
            }
        }
    }

    Closure errorHook = defaultErrorHook

    private void displayError(final Throwable cause) {
        if (errorHook == null) {
            throw new IllegalStateException("Error hook is not set")
        }

        errorHook.call(cause)
    }

    //
    // Interactive Shell
    //

    int run(final String[] args) {
        String commandLine = null

        if (args != null && args.length > 0) {
            commandLine = args.join(' ')
        }

        return run(commandLine as String)
    }

    int run(final String commandLine) {
        def term = Terminal.terminal

        if (log.debug) {
            log.debug("Terminal ($term)")
            log.debug("    Supported:  $term.supported")
            log.debug("    ECHO:       $term.echo (enabled: $term.echoEnabled)")
            log.debug("    H x W:      $term.terminalHeight x $term.terminalWidth")
            log.debug("    ANSI:       ${term.isANSISupported()}")

            if (term instanceof jline.WindowsTerminal) {
                log.debug("    Direct:     ${term.directConsole}")
            }
        }

        def code

        try {
            loadUserScript('groovysh.profile')

            // if args were passed in, just execute as a command
            // (but cygwin gives an empty string, so ignore that)
            if (commandLine != null && commandLine.trim().size() > 0) {
                // Run the given commands
                execute(commandLine)
            }
            else {
                loadUserScript('groovysh.rc')

                // Setup the interactive runner
                runner = new InteractiveShellRunner(this, this.&renderPrompt as Closure)

                // Setup the history
                runner.history = history = new History()
                runner.historyFile = new java.io.File(userStateDirectory, 'groovysh.history')

                // Setup the error handler
                runner.errorHandler = this.&displayError

                //
                // TODO: See if we want to add any more language specific completions, like for println for example?
                //

                // Display the welcome banner
                if (!io.quiet) {
                    def width = term.terminalWidth

                    // If we can't tell, or have something bogus then use a reasonable default
                    if (width < 1) {
                        width = 80
                    }

                    io.out.println("@|bold Calvino ${Calvino.version}|@ (JVM "+System.properties['java.version']+" r."+Calvino.released+")")
                    io.out.println("Running on " + System.properties['os.name'] + " " + System.properties['os.version'] + " (" + System.properties['os.arch'] + ")")
                    //io.out.println(messages['startup_banner.1'])
                    io.out.println('-' * (width - 1))
                    //try{
                    //    def q = (new Quote()).randomQuote()
                    //    io.out.println(Quote.addLinebreaks(q,width - 1))
                    //}
                    //catch(Exception e)
                    //{}
                    //io.out.println("")
                }

                // And let 'er rip... :-)
                runner.run()
            }

            code = 0
        }
        catch (ExitNotification n) {
            log.debug("Exiting w/code: ${n.code}")

            code = n.code
        }
        catch (Throwable t) {
            io.err.println(t.toString())
            t.printStackTrace(io.err)

            code = 1
        }

        assert code != null // This should never happen

        return code
    }
}

// AVVIATORE DELLA SHELL
class Start
{
    static {
        // Install the system adapters
        AnsiConsole.systemInstall()

        // Register jline ansi detector
        Ansi.setDetector(new AnsiDetector())
    }

    //private static final MessageSource messages = new MessageSource(Start.class)

    public static Calvinosh shell

    static void main(final String[] args) {
        IO io = new IO()
        Logger.io = io

        
        def cli = new CliBuilder(usage : 'groovysh [options] [...]', formatter: new HelpFormatter(), writer: io.out)

        cli.classpath("Specify where to find the class files - must be first argument")
        cli.cp(longOpt: 'classpath', "Aliases for '-classpath'")
        cli.h(longOpt: 'help', "Display this help message")
        cli.V(longOpt: 'version', "@|bold Calvino Shell|@ {0}")
        cli.v(longOpt: 'verbose', "Enable verbose output")
        cli.q(longOpt: 'quiet', "Suppress superfluous output")
        cli.d(longOpt: 'debug', "Enable debug output")
        cli.C(longOpt: 'color', args: 1, argName: 'FLAG', optionalArg: true, "Enable or disable use of ANSI colors")
        cli.D(longOpt: 'define', args: 1, argName: 'NAME=VALUE', "Define a system property")
        cli.T(longOpt: 'terminal', args: 1, argName: 'TYPE', "Specify the terminal TYPE to use")

        def options = cli.parse(args)

        if (options.h) {
            cli.usage()
            System.exit(0)
        }

        if (options.V) {
            io.out.println("Calvino ${Calvino.version}")
            System.exit(0)
        }

        if (options.hasOption('T')) {
            def type = options.getOptionValue('T')
            setTerminalType(type)
        }

        if (options.hasOption('D')) {
            def values = options.getOptionValues('D')

            values.each {
                setSystemProperty(it as String)
            }
        }

        if (options.v) {
            io.verbosity = IO.Verbosity.VERBOSE
        }

        if (options.d) {
            io.verbosity = IO.Verbosity.DEBUG
        }

        if (options.q) {
            io.verbosity = IO.Verbosity.QUIET
        }

        if (options.hasOption('C')) {
            def value = options.getOptionValue('C')
            setColor(value)
        }

        def code

        // Add a hook to display some status when shutting down...
        addShutdownHook {
            //
            // FIXME: We need to configure JLine to catch CTRL-C for us... Use gshell-io's InputPipe
            //

            if (code == null) {
                // Give the user a warning when the JVM shutdown abnormally, normal shutdown
                // will set an exit code through the proper channels

                io.err.println()
                io.err.println('@|bold WARNING:|@ Abnormal JVM shutdown detected')
            }

            io.flush()
        }
        

        SecurityManager psm = System.getSecurityManager()
        System.setSecurityManager(new NoExitSecurityManager())

        try {
            code = shell.run(options.arguments() as String[])
        }
        finally {
            System.setSecurityManager(psm)
        }

        // Force the JVM to exit at this point, since shell could have created threads or
        // popped up Swing components that will cause the JVM to linger after we have been
        // asked to shutdown

        System.exit(code)
    }

    static void setTerminalType(String type) {
        assert type != null
        
        type = type.toLowerCase();

        switch (type) {
            case 'auto':
                type = null;
                break;

            case 'unix':
                type = 'jline.UnixTerminal'
                break

            case 'win':
            case 'windows':
                type = 'jline.WindowsTerminal'
                break

            case 'false':
            case 'off':
            case 'none':
                type = 'jline.UnsupportedTerminal'
                // Disable ANSI, for some reason UnsupportedTerminal reports ANSI as enabled, when it shouldn't
                Ansi.enabled = false
                break;
        }

        if (type != null) {
            System.setProperty('jline.terminal', type)
        }
    }

    static void setColor(value) {
        if (value == null) {
            value = true // --color is the same as --color=true
        }
        else {
            value = Boolean.valueOf(value).booleanValue(); // For JDK 1.4 compat
        }

        Ansi.enabled = value
    }

    static void setSystemProperty(final String nameValue) {
        String name
        String value

        if (nameValue.indexOf('=') > 0) {
            def tmp = nameValue.split('=', 2)
            name = tmp[0]
            value = tmp[1]
        }
        else {
            name = nameValue
            value = Boolean.TRUE.toString()
        }

        System.setProperty(name, value)
    }
}

class AnsiDetector
    implements Callable<Boolean>
{
    public Boolean call() throws Exception {
        return Terminal.getTerminal().isANSISupported()
    }
}

//  CITAZIONI DI CALVINO DA WIKIQUOTE
public class Quote {
    public static quotes = []
    public Quote() {
        quotes.add("A un certo punto era solo questo rapporto a interessarmi, la mia storia diventava soltanto la storia della penna d\'oca della monaca che correva sul foglio bianco. (da \'I nostri antenati\', prefazione, p. XIX)")
        quotes.add("Avevo un paesaggio. Ma per poterlo rappresentare occorreva che esso diventasse secondario rispetto a qualcos\'altro: a delle persone, a delle storie. La Resistenza rappresentò la fusione tra paesaggio e persone. Da \'I sentieri dei nidi di ragno\', prefazione, 1964, citato in Pia Pera, \'Tra i nidi di ragno nel giardino di Calvino\', \'Il Sole 24 Ore\', 19 gennaio 2014. ")
        quotes.add("Beati quelli il cui atteggiamento verso la realtà è dettato da immutabili ragioni interiori! (da \'Un\'amara serenità\', in\'Una pietra sopra\', Einaudi, 1980)")
        quotes.add("(Su \'Cristo si è fermato a Eboli\') C\'è nel libro un alto livello intellettuale, vi si respira la cultura europea in cui Carlo Levi ha affondato le sue radici, diciamo la cultura europea fino a quell\'epoca, fino alla seconda guerra mondiale; c\'è la passione di sistemarne tutti i dati di un discorso coerente, e non ancora il timore di spezzare l\'armonia d\'una sistemazione con nuove acquisizioni, con nuove messe in questione; non ancora insomma l\'olimpicità culturalmente paga di se stessa che Carlo Levi si forgiò in seguito come una corazza contro tanta parte del problematismo contemporaneo. Con\'Paura della libertà\' la passione dell\'intelligenza in un momento di scacco generale muove a inglobare e classificare istituzioni, miti, personaggi storici, movimenti profondi dell\'animo umano. (da «Galleria», 3-6 (1967), pp. 237-40, a cura di Aldo Marcovecchio; citato in Carlo Levi, \'Cristo si è fermato a Eboli\', Einaudi, 1990, p. IX)")
        quotes.add("C\'è una persona che fa collezione di sabbia. Viaggia per il mondo, e quando arriva a una spiaggia marina, alle rive d\'un fiume o d\'un lago, a un deserto, a una landa, raccoglie una manciata d\'arena e se la porta con sé. Al ritorno, l\'attendono allineati in lunghi scaffali centinaia di flaconi di vetro entro i quali la fine sabbia grigia del Balaton, quella bianchissima del Golfo del Siam, quella rossa che il corso del Gambia deposita giù per il Senegal, dispiegano la loro non vasta gamma di colori sfumati, rivelano un\'uniformità da superficie lunare, pur attraverso le differenze di granulosità e consistenza, dal ghiaino bianco e nero del Caspio che sembra ancora inzuppato d\'acqua salata, ai minutissimi sassolini di Maratea, bianchi e neri anch \'essi, alla sottile farina bianca punteggiata di chiocciole viola di Turtle Bay, vicino a Malindi nel Kenia. (da \'Collezione di sabbia\')")
        quotes.add("Certe cose sulla vita partigiana nessuno le ha mai dette, nessuno ha mai scritto un racconto che sia anche la storia del sangue nelle vene, delle sostanze nell\'organismo. (da un colloquio con Ferdinando Camon,  citato in\'Corriere della sera\', 29 agosto 2007)")
        quotes.add("Certo nella sua opera (di Henry James), tutta sotto il segno dell\'elusività, del non detto, della ritrosia, questo (Daisy Miller) si presenta come uno dei racconti più chiari, con un personaggio di ragazza pieno di vita, eppure è un racconto non meno misterioso degli altri di questo introverso autore, tutto intessuto com\'è dei temi che s\'affacciano, sempre tra luce e ombra, lungo l\'intera sua opera. (dall\'introduzione a Henry James, \'Daisy Miller\', Baldini Castoldi, 2012)")
        quotes.add("Chi ha l\'occhio, trova quel che cerca anche a occhi chiusi. (da \'Marcovaldo\')")
        quotes.add("(Sull\'ispirazione) Ci metto molto a iniziare se ho l\'idea per un romanzo, trovo ogni scusa possibile per non lavorarci. Una volta iniziato, so essere molto veloce. (citato in\'Corriere della sera\', 30 marzo 2010)")
        quotes.add("Così decifrando il diario della melanconica (o felice?) collezionista di sabbia, sono arrivato a interrogarmi su cosa c\'è scritto in quella sabbia di parole scritte che ho messo in fila nella mia vita, quella sabbia che adesso mi appare tanto lontana dalle spiagge e dai deserti del vivere. Forse fissando la sabbia come sabbia, le parole come parole, potremo avvicinarci a capire come e in che misura in mondo triturato ed eroso possa ancora trovarvi fondamento e modello. (da \'Collezione di sabbia\')")
        quotes.add("È un\'energia volta verso l\'avvenire, ne sono sicuro, non verso il passato, quella che muove Orlando, Angelica, Ruggiero, Bradamante, Astolfo... (dai \'Saggi\', p. 75)")
        quotes.add("Era ora. Da vent \'anni la letteratura italiana ha uno scrittore che non assomiglia a nessun altro, inconfondibile in ogni sua frase, un inventore inesauribile e irresistibile nel gioco del linguaggio e delle idee... Manganelli è il più italiano degli scrittori e nello stesso tempo il più isolato nella letteratura italiana. (dall\'Introduzione a Giorgio Manganelli, \'Centuria\', Adelphi, Milano 1995)")
        quotes.add("Ernest Hemingway è figlio delle contraddizioni di un\'epoca: ribelle e denunciatore per un verso, ma per un altro senza fiducia in un avvenire, e invece disperatamente in cerca di un mito oscuro di antichità: l\'Europa, il Cattolicesimo, l\'Italia, la Spagna. (citato in Giuseppe Trevisani, \'Hemingway\', CEI, Milano 1966)")
        quotes.add("Il genere umano è una zona del vivente che va definita circoscrivendone i confini. (dall\'introduzione a Plinio il Vecchio, \'Storia naturale\', Einaudi)")
        quotes.add("Il luogo ideale per me è quello in cui è più naturale vivere da straniero. (da \'Eremita a Parigi. Pagine autobiografiche\', Palomar-Mondadori, Milano 1994)")
        quotes.add("Solo dopo aver conosciuto la superficie delle cose, – conclude – ci si può spingere a cercare quel che c\'è sotto. Ma la superficie delle cose è inesauribile. (\'Palomar\', Einaudi, 1983).")
        quotes.add("Il primo libro sarebbe meglio non averlo mai scritto. Finché il primo libro non è scritto, si possiede quella libertà di cominciare che si può usare una sola volta nella vita, il primo libro già ti definisce mentre tu in realtà sei ancora lontano dall\'esser definito; e questa definizione poi dovrai portartela dietro per la vita, cercando di darne conferma o approfondimento o correzione o smentita, ma mai più riuscendo a prescinderne. (dalla prefazione del 1964 a \'Il sentiero dei nidi di ragno\')")
        quotes.add("Il vento, venendo in città da lontano, le porta doni inconsueti, di cui s\'accorgono solo poche anime sensibili, come i raffreddati del fieno, che starnutano per pollini di fiori d\'altre terre. (da \'Marcovaldo\')")
        quotes.add("Io credo alla pedagogia repressiva. Mi rendo conto di essere molto antiquato in questo, ma continuo ad essere convinto che resti il miglior metodo d\'educazione alla cultura. (citato in Luca Clerici, Bruno Falcetto, \'Calvino & l\'editoria\', Marcos y Marcos, 1993)")
        quotes.add("Io sono la pecora nera, l\'unico letterato della famiglia. Da AA. VV., \'Ritratti su misura di scrittori italiani\', a cura di Elio Filippo Accrocca, Sodalizio del Libro, Venezia, 1960; ora \'Ritratto su misura\', in\'Eremita a Parigi\', Mondadori, 2010. ")
        quotes.add("Nell\'eros come nella ghiottoneria, il piacere è fatto di precisione. (dall\'introduzione a Charles Fourier, \'Teoria dei quattro movimenti\', Einaudi, 1971)")
        quotes.add("L\'inconscio è il mare del non dicibile, dell\'espulso fuori dai confini del linguaggio, del rimosso in seguito ad antiche proibizioni. (da \'Cibernetica e fantasmi\', in\'Saggi\')")
        quotes.add("L\'opera letteraria potrebbe esser definita come un\'operazione nel linguaggio scritto che coinvolge contemporaneamente più livello di realtà [...] la letteratura non conosce la realtà ma solo livelli. [...] La letteratura conosce la realtà dei livelli e questa è una realtà che conosce forse meglio di quanto non s\'arrivi a conoscerla attraverso altri procedimenti conoscitivi. È già molto. (da \'Tre correnti del romanzo italiano d\'oggi\', in\'Saggi\', pp. 381, 398)")
        quotes.add("La conoscenza del prossimo ha questo di speciale: passa necessariamente attraverso la conoscere se stessi. (da \'Palomar\')")
        quotes.add("La vita d\'una persona consiste in un insieme d\'avvenimenti di cui l\'ultimo potrebbe anche cambiare il senso di tutto l\'insieme, non perché conti di più dei precedenti ma perché inclusi in una vita gli avvenimenti si dispongono in un ordine che non è cronologico, ma risponde a un\'architettura interna. (da \'Palomar\')")
        quotes.add("Lord Jim è un giovane che abbraccia la carriera di capitano marittimo con un ideale di eroismo e abnegazione... Egli punta tutto sul grande momento della prova suprema in cui dimostrerà tutto il suo valore.<ref name=jim>Citato in Federica Almagioni, \'prefazione\' a Joseph Conrad, \'Lord Jim\', traduzione di Alessandro Gallone, Alberto Peruzzo Editore, 1989. ")
        quotes.add("(Thomas Mann) Lui capì tutto o quasi del nostro mondo, ma sporgendosi da un\'estrema ringhiera dell\'Ottocento. Noi vediamo il mondo precipitando nella tromba delle scale. (da \'La giornata d\'uno scrutatore\')")
        quotes.add("Nella mia vita ho incontrato donne di grande forza. Non potrei vivere senza una donna al mio fianco. Sono solo un pezzo d\'un essere bicefalo e bisessuato, che è il vero organismo biologico e pensante. (dall\'intervista di Ludovica Ripa di Meana, \'Se una sera d\'autunno uno scrittore\', \'L\'Europeo\', 17 novembre 1980)")
        quotes.add("\'Papà\' dissero i bambini, \'le mucche sono come i tram? Fanno le fermate? Dov\'è il capolinea delle mucche? Niente a che fare coi tram\' spiegò Marcovaldo, \'vanno in montagna. Si mettono gli sci?\' chiese Pietruccio.\'Vanno al pascolo a mangiare l\'erba. E non gli fanno la multa se sciupano i prati?\' (da \'Marcovaldo\')")
        quotes.add("Oggi il linguaggio politico italiano si è molto complicato, tecnicizzato, intellettualizzato, e credo che tenda a saldarsi in un arco che comprende cattolici e marxisti [...] più a non dire che a dire [...] il linguaggio \'obiettivo\' del telegiornale, quando riassume i discorsi dei leaders politici: tutti ridotti a minime variazioni della stessa combinazione di termini anodini, incolori e insapori. Insomma, il vocabolo semanticamente più povero viene sempre preferito a quello semanticamente più pregnante.  (da \'Contemporaneo\', 5; citato in Fochi 1966, p. 264)")
        quotes.add("Per il giovane, la donna è quel che sicuramente c\'è. (dall\'introduzione a \'I nostri antenati\')")
        quotes.add("Quando ho cominciato a scrivere \'Il visconte dimezzato\', volevo soprattutto scrivere una storia divertente per divertire me stesso e possibilmente anche gli altri; avevo questa immagine di un uomo tagliato in due ed ho pensato che questo tema dell\'uomo tagliato in due, dell\'uomo dimezzato fosse un tema significativo, avesse un significato contemporaneo: tutti ci sentiamo in qualche modo incompleti, tutti realizziamo una parte di noi stessi e non l\'altra. (da un\'intervista con gli studenti di Pesaro, 11 maggio 1983, in\'Il gusto dei contemporanei\', Quaderno n. 3, Italo Calvino, Pesaro 1987, p. 9)")
        quotes.add("Questa dell\'amore per le cose di cui parla è una caratteristica che bisogna tener presente se si vuole riuscire a definire la singolarità dell\'operazione letteraria di Levi. Perché quest \'uomo che si dice sempre che mette se stesso al centro d\'ogni narrazione, che fa scaturire sempre attorno alla sua presenza incontri straordinari, è poi lo scrittore piú dedito alle cose, al mondo oggettivo, alle persone. Il suo metodo è di descrivere con rispetto e devozione ciò che vede, con uno scrupolo di fedeltà che gli fa moltiplicare particolari e aggettivi. La sua scrittura è un puro strumento di questo suo rapporto amoroso col mondo, di questa fedeltà agli oggetti della sua rappresentazione. (da «Galleria», 3-6 (1967), pp. 237-40, a cura di Aldo Marcovecchio; citato in Carlo Levi, \'Cristo si è fermato a Eboli\', Einaudi, 1990, p. XII)")
        quotes.add("(Rocco Scotellaro) era una testa solida il ragazzo di Tricarico uno di quei tipi che hanno sempre qualche idea loro da darti e qualche idea tua da farti germogliare in mente.")
        quotes.add("Tutta la sua opera mira all\'enciclopedia [...]. Si parla sempre dell\'impareggiabile fantasia di Jules Verne nel prevedere le invenzioni scientifiche. In realtà era un grande lettore di riviste scientifiche, che arricchiva di quello che via via veniva a sapere sulle ricerche in corso. (da \'Viaggio al centro di Jules Verne\', in\'la Repubblica\', 29 gennaio 1978)")
        quotes.add("Tutto può cambiare, ma non la lingua che ci portiamo dentro, anzi che ci contiene dentro di sé come un mondo più esclusivo e definitivo del ventre materno. (da \'Eremita a Parigi\')")
        quotes.add("Un libro straordinario, \'Centuria\', la cui ricchezza di motivi non posso propormi d\'esplorare in questa nota, intesa solo a offrire un inquadramento generale dell\'opera di Giorgio Manganelli e a invitare a valicarne la soglia. (citato in Giorgio Manganelli, \'Centuria\', Adelphi, Milano 1995)")
        quotes.add("Zig zag tracciato dai cavalli al galoppo e dalle intermittenze del cuore umano. (da \'Ariosto geometrico\', in\'Italianistica\', settembre-ottobre 1974)")
        quotes.add("Un libro (io credo) è qualcosa con un principio e una fine (anche se non è un romanzo in senso stretto), è uno spazio in cui il lettore deve entrare, girare, magari perdersi, ma a un certo punto trovare un\'uscita, o magari parecchie uscite, la possibilità di aprirsi una strada per venirne fuori. (da una conferenza tenuta il 29 marzo 1983 agli studenti della Columbia University di New York, citato nell\'introduzione a \'Le città invisibili\', Mondadori, 2012, p. VI)")
        quotes.add("La vita, pensò il nudo, era un inferno, con rari richiami d\'antichi felici paradisi. (da \'Uno dei tre è ancora vivo\': p. 82)")
        quotes.add("«Ogni paese, – pensò, – anche quello che pare più ostile e disumano, ha due volti; a un certo punto finisci per scoprire quello buono, che c\'era sempre stato, solo che tu non lo vedevi e non sapevi sperare.» (da \'Paese infido\': p. 98)")
        quotes.add("Così la macchina dell\'oppressione sempre si volta contro chi la serve. (da \'La gallina di reparto\')")
        quotes.add("Ma tale inesauribile cosa è la libertà dell\'uomo, che pure in queste condizioni il pensiero di Pietro riusciva a tessere la sua ragnatela da una macchina all\'altra, a fluire continuo come il filo di bocca al ragno, e in mezzo a quella geometria di passi gesti sguardi e riflessi egli a tratti si ritrovava padrone di sé e tranquillo come un nonno campagnolo che esce di mattino tardo sotto la pergola, e mira il sole, e fischia al cane, e sorveglia i nipoti che si dondolano ai rami, e guarda giorno per giorno maturare i fichi. (da \'La gallina di reparto\': p. 205)")
        quotes.add("Mentone non mi attraeva più: perché io avevo ancora quel bisogno d\'amici che è proprio dei giovani, cioè il bisogno di dare un senso a quel che vivono parlandone con altri; ossia ero lontano dall\'autosufficienza virile, che s\'acquista con l\'amore, fatto insieme d\'integrazione e solitudine. (da \'Gli avanguardisti a Mentone\': p. 280)")
        quotes.add("C\'era la guerra, e tutti ne eravamo presi, e ormai sapevo che avrebbe deciso delle nostre vite. Della mia vita; e non sapevo come. (da \'Gli avanguardisti a Mentone\': p. 298)")
        quotes.add("La preseza di estranei addormentati suscita negli animi onesti un naturale rispetto, e noi nostro malgrado ne eravamo intimiditi. (da \'Le notti dell\'UNPA\': p. 310)")
        quotes.add("Amedeo si sentiva in una condizione perfetta: la pagina scritta gli apriva la vera vita, profonda e appassionante, e alzando gli occhi ritrovava un casuale ma gradevole accostarsi di colori e  sensazioni, un mondo accessorio e decorativo, che non poteva impegnarlo in nulla. (da \'L\'avventura di un lettore\': p. 365)")
        quotes.add("Di fatto, ogni silenzio consiste nella rete di rumori minuti che l\'avvolge: il silenzio dell\'isola si staccava da quello del calmo mare circostante perché era percorso da fruscii vegetali, da versi d\'uccelli o da un improvviso frullo d\'ali. (da \'L\'avventura di un poeta\': p. 398)")
        quotes.add("Quinto reagiva sempre buttandosi dall\'altra parte, abbracciando tutto quello che era nuovo, in contrasto, tutto quel che faceva violenza, e anche adesso, lì, a scoprire l\'avvento d\'una classe nuova del dopoguerra, d\'imprenditori improvvisati e senza scrupoli, egli si sentiva preso da qualcosa che somigliava ora a un interesse scientifico («assistiamo a un importante fenomeno sociologico, mio caro...») ora a un contraddittorio compiacimento estetico. La squallida invasione del cemento aveva il volto camuso e informe dell\'uomo nuovo Caisotti. (da \'La speculazione edilizia\': p. 453)")
        quotes.add("«Un tempo solo chi godeva d\'una rendita agricola poteva fare l\'intellettuale, – pensò Quinto. – La cultura paga ben caro l\'essersi liberata da una base economica. Prima viveva sul privilegio, però aveva radici solide. Ora gli intellettuali non sono borghesi e non sono proletari.»  (da \'La speculazione edilizia\': p. 462)")
        quotes.add("E io restavo senza parola, perché capivo che la cucina era il solo luogo di tutta la casa in cui quella donna veramente vivesse, e il resto, le stanze adorne e continuamente spazzolate e incerate erano una specie di opera d\'arte in cui lei riversava tutti i suoi sogni di bellezza, e per coltivare la perfezione di quelle stanze si condannava a non viverci, a non entrarci mai come padrona ma solo come donna di fatica, e il resto della giornata a passarlo nell\'unto e nella polvere. (da \'La nuvola di smog\': p. 531)")
        quotes.add("Ci sono quelli che si  condannano al grigiore della vita più mediocre perché hanno avuto un dolore, una sfortuna; ma ci sono anche quelli che lo fanno perché hanno avuto più fortuna di quella che si sentivano di reggere. (da \'La nuvola di smog\': p. 541)")
        quotes.add("Chi vuole guardare bene la terra deve tenersi alla distanza necessaria. (da Il barone rampante)")
        quotes.add("I rivoluzionari sono più formalisti dei conservatori.(da Il barone rampante)")
        quotes.add("Il bassotto alzò il muso verso di lui, con lo sguardo dei cani quando non capiscono e non sanno che possono aver ragione a non capire. (da Il barone rampante)")
        quotes.add("Le imprese che si basano su di una tenacia interiore devono essere mute e oscure; per poco uno le dichiari o se ne glori, tutto appare fatuo, senza senso o addirittura meschino. (da Il barone rampante)")
        quotes.add("Nessun Cosimo potrà più incedere per gli alberi. (da Il barone rampante)")
        quotes.add("Quando ho più idee degli altri, do agli altri queste idee, se le accettano; e questo è comandare.  (da Il barone rampante)")
        quotes.add("Viviamo in un paese dove si verificano sempre le cause e non gli effetti. (da Il barone rampante) ")
        quotes.add("Ma in tutta quella smania c\'era un\'insoddisfazione più profonda, una mancanza, in quel cercare gente che l\'ascoltasse c\'era una ricerca diversa. Cosimo non conosceva ancora l\'amore, e ogni esperienza senza quello che è? che vale aver rischiato la vita, quando ancora della vita non conosci il sapore? (da Il barone rampante)")
        quotes.add("Lui conobbe lei e se stesso, perché in verità non s\'era mai saputo. E lei conobbe lui e se stessa, perché pur essendosi saputa sempre, mai s\'era potuta riconoscere così. (da Il barone rampante)")
        quotes.add(" Cosimo tutti i giorni era sul frassino a guardare il prato come se in esso potesse leggere qualcosa che da tempo lo struggeva dentro: l\'idea stessa della lontananza, dell\'incolmabilità, dell\'attesa che può prolungarsi oltre la vita. (da Il barone rampante)")
        quotes.add(" L\'Abate passò il resto dei suoi giorni tra carcere e convento in continui atti d\'abiura, finché non morì, senza aver capito, dopo una vita intera dedicata alla fede, in che cosa mai credesse, ma cercando di credervi fermamente fino all\'ultimo. (da Il barone rampante)")
        quotes.add(" Anche quando pare di poche spanne, un viaggio può restare senza ritorno. (da Il barone rampante)")
        quotes.add("La forza dell\'eremita si misura non da quanto lontano è andato a stare, ma dalla poca distanza che gli basta per staccarsi dalla città, senza mai perderla di vista. (da Il Castello dei Destini Incrociati)")
        quotes.add("C\'è un modo colpevole di abitare la città: accettare le condizioni della bestia feroce dandogli in pasto i nostri figli. C\'è un modo colpevole di abitare la solitudine: credersi tranquillo perché la bestia feroce è resa inoffensiva da una spina nella zampa. L\'eroe della storia è colui che nella città punta la lancia nella gola del drago, e nella solitudine tiene con sé il leone nel pieno delle sue forze, accettandolo come custode e genio domestico, ma senza nascondersi la sua natura di belva. (da Il Castello dei Destini Incrociati)")
        quotes.add("Uno stemma tra due lembi d\'un ampio manto drappeggiato, e dentro lo stemma s\'aprivano altri due lembi di manto con in mezzo uno stemma più piccolo, [...] e in mezzo ci doveva essere chissà che cosa, ma non si riusciva a scorgere. (Il cavaliere inesistente p. 5)")
        quotes.add("Cosa può sapere del mondo una povera suora? (Il cavaliere inesistente p. 32)")
        quotes.add("Forse questi suoi vagheggiamenti di severità e rigore se li era messi in testa per contrastare la sua vera natura. Per esempio, se c\'era una sciattona in tutto l\'esercito di Francia, era lei. (Il cavaliere inesistente p. 56)")
        quotes.add("Cosí sempre corre il giovane verso la donna: ma è davvero amore per lei a spingerlo? O non è amore soprattutto di sé, ricerca d\'una certezza d\'esserci che solo la donna gli può dare? (Il cavaliere inesistente p. 57)   ")
        quotes.add("Questa storia che ho intrapreso a scrivere è ancora più difficile di quanto io non pensassi. Ecco che mi tocca rappresentare la più gran follia dei mortali, la passione amorosa, dalla quale il voto, il chiostro e il naturale pudore m \'hanno fin qui scampata. [...] Dunque anche dell\'amore come della guerra dirò alla buona quel che riesco a immaginarne: l\'arte di scriver storie sta nel saper tirar fuori da quel nulla che si è capito della vita tutto il resto; ma finita la pagina si riprende la vita e ci s\'accorge che quel che si sapeva è proprio un nulla. (Il cavaliere inesistente cap. VI)")
        quotes.add("A ognuna è data la sua penitenza, qui in convento, il suo modo di guadagnarsi la salvezza eterna. A me è toccata questa di scriver storie: è dura, è dura. [...] Ma la nostra santa vocazione vuole che si anteponga alle caduche gioie del mondo qualcosa che poi resta. Che resta... se poi anche questo libro, e tutti i nostri atti di pietà, compiuti con cuori di cenere, non sono già cenere anch \'essi... più cenere degli atti sensuali là nel fiume, che trepidano di vita e si propagano come cerchi nell\'acqua... Ci si mette a scrivere di lena, ma c\'è un\'ora in cui la penna non gratta che polveroso inchiostro, e non vi scorre più una goccia di vita, e la vita è tutta fuori, fuori dalla finestra, fuori di te, e ti sembra che mai più potrai rifugiarti nella pagina che scrivi, aprire un altro mondo, fare il salto. Forse è meglio così: forse quando scrivevi con gioia non era miracolo né grazia: era peccato, idolatria, superbia. Ne sono fuori, allora? No, scrivendo non mi sono cambiata in bene: ho solo consumato un po \' d\'ansiosa incosciente giovinezza. Che mi varranno queste pagine scontente? Il libro, il voto, non varrà più di quanto tu vali. Che ci si salvi l\'anima scrivendo non è detto. Scrivi, scrivi, e già la tua anima è persa. (Il cavaliere inesistente cap. VII)")
        quotes.add("Forse non è stata scelta male questa mia penitenza, dalla madre badessa: ogni tanto mi accorgo che la penna ha preso a correre sul foglio come da sola, e io a correrle dietro. È verso la verità che corriamo, la penna e io, la verità che aspetto sempre che mi venga incontro, dal fondo d\'una pagina bianca, e che potrò raggiungere soltanto quando a colpi di penna sarò riuscita a seppellire tutte le accidie, le insoddisfazioni, l\'astio che sono qui chiusa a scontare. (Il cavaliere inesistente cap. VIII)")
        quotes.add("E lentamente, senza gualcire le lenzuola, entrò armato di tutto punto e si stese composto come in un sepolcro. (Il cavaliere inesistente p. 91)  ")
        quotes.add("Io che scrivo questo libro seguendo su carte quasi illeggibili una antica cronaca, mi rendo conto solo adesso che ho riempito pagine e pagine e sono ancora al principio della mia storia, ora comincia il vero svolgimento della vicenda, cioè gli avventurosi viaggi di Agilulfo e del suo scudiero [...] Ecco come questa disciplina di scrivana da convento e l\'assidua penitenza del cercare parole e il meditare la sostanza ultima delle cose m \'hanno mutata: quello che il volgo – ed io stessa fin qui – tiene per massimo diletto, cioè l\'intreccio d\'avventure in cui consiste ogni romanzo cavalleresco, ora mi pare una guarnizione superflua, un freddo fregio, la parte più ingrata del mio penso. (Il cavaliere inesistente cap. IX)")
        quotes.add("(L\'armatura, prima candida, senza un graffio è) incrostata di terra, spruzzata di sangue nemico, costellata d\'ammaccature, bugni, sgraffi, slabbri. (Il cavaliere inesistente p. 118)")
        quotes.add("Ecco, o futuro, sono salita in sella al tuo cavallo. Quali nuovi stendardi mi levi incontro dai pennoni delle torri di città non ancora fondate? quali fiumi di devastazioni dai castelli e dai giardini che amavo? quali impreviste età dell\'oro prepari, tu malpadroneggiato, tu foriero di tesori pagati a caro prezzo, tu mio regno da conquistare, futuro... (Il cavaliere inesistente p. 125)")
        quotes.add("Se infelice è l\'innamorato che invoca baci di cui non sa il sapore, mille volte più infelice è chi questo sapore gustò appena e poi gli fu negato. (Il cavaliere inesistente cap. XI)")
        quotes.add("– Non c\'è difesa né offesa, non c\'è senso di nulla, – disse Torrismondo. – La guerra durerà fino alla fine dei secoli e nessuno vincerà o perderà, resteremo fermi gli uni di fronte agli altri per sempre. E senza gli uni gli altri non sarebbero nulla e ormai sia noi che loro abbiamo dimenticato perché combattiamo... (Il cavaliere inesistente p. 62)")
        quotes.add("Così sempre corre il giovane verso la donna: ma è davvero amore per lei a spingerlo? o non è amore soprattutto di sé, ricerca d\'una certezza d\'esserci che solo la donna gli può dare? Corre e s\'innamora il giovane, insicuro di sé, felice e disperato, e per lui la donna è quella che certamente c\'è, e lei sola può dargli quella prova. Ma la donna anche lei c\'è e non c\'è: eccola di fronte a lui, trepidante anch \'essa, insicura, come fa il giovane a non capirlo? Cosa importa chi trai due è il forte e chi il debole? Sono pari. Ma il giovane non lo sa perché non vuole saperlo: quella di cui ha fame è la donna che c\'è, la donna certa. Lei invece sa più cose; o meno; comunque sa cose diverse; ora è un diverso modo d\'essere che cerca. (Il cavaliere inesistente cap. VI)")
        quotes.add(" E così imperversa e non si dà ragione e a un certo punto l\'innamoramento di lei è pure innamoramento di sé, di sé innamorato di lei, è innamoramento di quel che potrebbero essere loro due insieme, e non sono. (Il cavaliere inesistente cap. VII)")
        quotes.add(" Anche ad essere si impara... (Il cavaliere inesistente cap. XI)")
        quotes.add(" «Tu butti fuori certi peti piú puzzolenti dei miei, cadavere. Non so perché tutti ti compiangano. Cosa ti manca? Prima ti muovevi, ora il tuo movimento passa ai vermi che tu nutri. Crescevi unghie e capelli: ora colerai liquame che farà crescere piú alte nel sole le erbe del prato. Diventerai erba, poi latte delle mucche che mangeranno l\'erba, sangue di bambino che ha bevuto il latte, e così via. Vedi che sei piú bravo a vivere tu di me, o cadavere?» (Il cavaliere inesistente p. 64)")
        quotes.add(" Contro a tutte le regole imperiali d\'etichetta, Carlomagno s\'andava a mettere a tavola prima dell\'ora, quando ancora non c\'erano altri commensali. Si siede e comincia a spiluzzicare pane o formaggio o olive o peperoncini, insomma tutto quel che è già in tavola. Non solo, ma si serve con le mani. Spesso il potere assoluto fa perdere ogni freno anche ai sovrani piú temperanti e genera l\'arbitrio. (Il cavaliere inesistente p. 80)")
        quotes.add("Nulla piace agli uomini quanto avere dei nemici e vedere se sono proprio come ci s\'immagina. (Il visconte dimezzatocap. II)")
        quotes.add("Io ero intero e non capivo, e mi muovevo sordo e incomunicabile tra i dolori e le ferite seminati dovunque, là dove meno da intero uno osa credere. (Il visconte dimezzato cap VII)")
        quotes.add("I nostri sentimenti si facevano incolori e ottusi, poiché ci sentivamo come perduti tra malvagità e virtù ugualmente disumane. (Il visconte dimezzato cap. IX)")
        quotes.add("Alle volte uno si crede incompleto ed è soltanto giovane. (Il visconte dimezzato cap. X)")
        quotes.add("Ma già le navi stavano scomparendo all\'orizzonte e io rimasi qui, in questo nostro mondo pieno di responsabilità e fuochi fatui. (Il visconte dimezzato cap. X)")
        quotes.add("Oh Pamela, questo è il bene dell\'esser dimezzato: il capire d\'ogni persona e cosa al mondo la pena che ognuno e ognuna ha per la propria incompletezza. (Il visconte dimezzato cap. VII)")
        quotes.add("Non io solo, Pamela, sono un essere spaccato e divelto, ma tu pure e tutti. Ecco io ora ho una fraternità che prima, da intero, non conoscevo: quella con tutte le mutilazioni e sofferenze del mondo. (Il visconte dimezzato cap. VII)")
        quotes.add(" Se verrai con me, Pamela, imparerai a soffrire dei mali di ciascuno e a curare i tuoi curando i loro. (Il visconte dimezzato cap. VII)")
        quotes.add("Forse ci s\'aspettava che, tornato intero il visconte, s\'aprisse un\'epoca di felicità meravigliosa; ma è chiaro che non basta un visconte completo perché diventi completo tutto il mondo. (Il visconte dimezzato cap. X)")
        quotes.add(" S \'era messo a capo d\'una banda di ragazzi cattolici che saccheggiavano le campagne attorno; e non solo spogliavano gli alberi da frutta, ma entravano anche nelle case e nei pollai. E bestemmiavano più forte e più sovente perfino di Mastro Pietrochiodo: sapevano tutte le bestemmie cattoliche e ugonotte, e se le scambiavano tra loro. – Ma faccio anche tanti altri peccati, – mi spiegò, dico falsa testimonianza, mi dimentico di dar acqua ai fagioli, non rispetto il padre e la madre, torno a casa la sera tardi. Adesso voglio fare tutti i peccati che ci sono; anche quelli che dicono che non sono abbastanza grande per capire.  – Tutti i peccati? – io gli dissi. – Anche ammazzare?  Si strinse nelle spalle: – Ammazzare adesso non mi conviene e non mi serve.  – Mio zio ammazza e fa ammazzare per gusto, dicono, – feci io, per aver qualcosa da parte mia da contrapporre a Esaù.  Esaù sputò.  – Un gusto da scemi, – disse. (Il visconte dimezzato p. 95)")
        quotes.add("E come chi, tuffandosi nell\'acqua fredda, s\'è sforzato di convincersi che il piacere di tuffarsi sia tutto in quell\'impressione di gelo, e poi nuotando ritrova dentro di sé il calore e insieme il senso di quanto fredda e ostile è l\'acqua, così Amerigo dopo tutte le operazioni mentali per trasformare dentro di sé lo squallore della sezione elettorale in un valore prezioso, era tornato a riconoscere che la prima impressione -di estraneità e freddezza di quell\'ambiente- era la giusta.  (da La giornata di uno scrutatore)")
        quotes.add("E che cos\'era se non il caso ad aver fatto di lui Amerigo Ormea un cittadino responsabile, un elettore cosciente, partecipe del potere democratico, di qua del tavolo del seggio, e non -di là del tavolo-, per esempio, quell\'idiota che veniva avanti ridendo come se giocasse? (da La giornata di uno scrutatore)")
        quotes.add("Non sapeva cosa avrebbe voluto: capiva solo quant\'era distante, lui come tutti, dal vivere come va vissuto quello che cercava di vivere. (da La giornata di uno scrutatore)")
        quotes.add("Ecco, pensò Amerigo, quei due, così come sono, sono reciprocamente necessari. E pensò: ecco, questo modo d\'essere è l\'amore. E poi: l\'umano arriva dove arriva l\'amore; non ha confini se non quelli che gli diamo. (da La giornata di uno scrutatore)")
        quotes.add("«Forse mentre noi parliamo sta affiorando sparsa entro i confini del tuo impero; puoi rintracciarla, ma a quel modo che t \'ho detto. Già il Gran Kan stava sfogliando nel suo atlante le carte della città che minacciano negli incubi e nelle maledizioni: Enoch, Babilonia, Yahoo, Butua, Brave New World. Dice:- Tutto è inutile, se l\'ultimo approdo non può essere che la città infernale, ed è là in fondo che, in una spirale sempre più stretta, ci risucchia la corrente.» (da Le città invisibili)")
        quotes.add("Le immagini della memoria, una volta fissate con le parole, si cancellano... Forse Venezia ho paura di perderla tutta in una volta, se ne parlo. O forse parlando d\'altre città, l\'ho già perduta a poco a poco. (da Le città invisibili) ")
        quotes.add("L\'inferno dei viventi non è qualcosa che sarà; se ce n\'è uno, è quello che è già qui, l\'inferno che abitiamo tutti i giorni, che formiamo stando insieme. Due modi ci sono per non soffrirne. Il primo riesce facile a molti: accettare l\'inferno e diventarne parte fino al punto di non vederlo più. Il secondo è rischioso ed esige attenzione e apprendimento continui: cercare e saper riconoscere chi e cosa, in mezzo all\'inferno, non è inferno, e farlo durare, e dargli spazio. (da Le città invisibili) ")
        quotes.add("L\'occhio non vede cose ma figure di cose che significano altre cose. (da Le città invisibili) ")
        quotes.add("L\'ordine degli dei è proprio quello che si rispecchia nella città dei mostri. (da Le città invisibili) ")
        quotes.add("Marco Polo descrive un ponte, pietra per pietra.– Ma qual è la pietra che sostiene il ponte? – chiede Kublai Kan.– Il ponte non è sostenuto da questa o quella pietra, – risponde Marco, – ma dalla linea dell\'arco che esse formano.Kublai Kan rimane silenzioso, riflettendo.Poi soggiunge: – Perché mi parli delle pietre? È solo dell\'arco che mi importa.Polo risponde: – Senza pietre non c\'è arco. (da Le città invisibili) ")
        quotes.add("Non c\'è linguaggio senza inganno. (da Le città invisibili) ")
        quotes.add("Ogni città riceve la sua forma dal deserto a cui si oppone. (da Le città invisibili) ")
        quotes.add("Ogni volta che si entra nella piazza ci si trova in mezzo ad un dialogo. (da Le città invisibili)")
        quotes.add("È delle città come dei sogni: tutto l\'immaginabile può essere sognato ma anche il sogno più inatteso è un rebus che nasconde un desiderio oppure il suo rovescio, una paura. Le città come i sogni sono costruite di desideri e di paure. (da Le città invisibili) ")
        quotes.add("D\'una città non godi le sette o le settantasette meraviglie, ma la risposta che dà a una tua domanda. (da Le città invisibili) ")
        quotes.add("Gli antichi costruirono Valdrada sulle rive di un lago con case, tutte verande una sopra l\'altra e vie alte che affacciano sull\'acqua i parapetti a balaustra. Così il viaggiatore vede arrivando due città: una diritta sopra il lago e una riflessa capovolta. Non esiste o avviene cosa nell\'una Valdrada che l\'altra Valdrada non ripeta, perché la città fu costruita in modo che ogni suo punto fosse riflesso dal suo specchio, e la Valdrada giù nell\'acqua contiene non solo tutte le scanalature e gli sbalzi delle facciate che s\'elevano sopra il lago ma anche l\'interno delle stanze con i soffitti e i pavimenti, la prospettiva dei corridoi, gli specchi degli armadi. (da Le città invisibili) ")
        quotes.add("La menzogna non è nel discorso, è nelle cose. (da Le città invisibili) ")
        quotes.add("Viaggiando ci s\'accorge che le differenze si perdono: ogni città va somigliando a tutte le città, i luoghi si scambiano forma ordine distanze, un pulviscolo informe invade i continenti. (da Le città invisibili)")
        quotes.add("Per tutti presto o tardi viene il giorno in cui abbassiamo lo sguardo lungo i tubi delle grondaie e non riusciamo più a staccarlo dal selciato. (da Le città invisibili)")
        quotes.add("Chi comanda al racconto non è la voce: è l\'orecchio. (da Le città invisibili)")
        quotes.add("Chiese a Marco Kublai: – Tu che esplori intorno e vedi i segni, saprai dirmi verso quale futuro ci spingono i venti propizi.  – Per questi porti non saprei tracciare la rotta sulla carta né fissare la data dell\'approdo. Alle volte mi basta uno scorcio che s\'apre nel bel mezzo d\'un paesaggio incongruo un affiorare di luci nella nebbia, il dialogo di due passanti che s\'incontrano nel viavai, per pensare che da lì metterò assieme pezzo a pezzo la città perfetta, fatta di frammenti mescolati col resto, d\'istanti separati da intervalli, di segnali che uno manda e  non sa chi li raccoglie. Se ti dico che la città cui tende il mio viaggio è discontinua nello spazio e nel tempo, ora più rada ora più densa, tu non devi credere che si possa smettere di cercarla. Forse mentre noi parliamo sta affiorando sparsa entro i confini del tuo impero; puoi rintracciarla, ma a quel modo che t \'ho detto. (da Le città invisibili)")
        quotes.add("Così, a cavallo del nostro secchio, ci affacceremo al nuovo millennio, senza sperare di trovarvi nulla di più di quello che saremo capaci di portarvi. (da Lezioni americane, \'Leggerezza\')")
        quotes.add("Credo che la mia prima spinta venga da una mia ipersensibilità o allergia: mi sembra che il linguaggio venga sempre usato in modo approssimativo, casuale, sbadato, e ne provo un fastidio intollerabile. Non si creda che questa mia reazione corrisponda a un\'intolleranza per il prossimo: il fastidio peggiore lo provo sentendo parlare me stesso. (da Lezioni americane,  \'Esattezza\')")
        quotes.add("Fino al momento precedente a quello in cui cominciamo a scrivere, abbiamo a nostra disposizione il mondo [...] il mondo dato in blocco, senza né un prima né un poi, il mondo come memoria individuale e come potenzialità implicita [...]. Ogni volta l\'inizio è quel momento di distacco dalla molteplicità dei possibili: per il narratore è l\'allontanare da sé la molteplicità delle storie possibili, in modo da isolare e rendere raccontabile la singola storia che ha deciso di raccontare. (da \'Appendice\', v. 1, pp. sgg.)")
        quotes.add("In ogni caso il racconto è un\'operazione sulla durata, un incantesimo che agisce sullo scorrere del tempo, contraendolo o dilatandolo. (da Lezioni americane,  \'Rapidità\')")
        quotes.add("La fantasia è un posto dove ci piove dentro. (da Lezioni americane, \'Visibilità\')")
        quotes.add("L\'eccessiva ambizione dei propositi può essere rimproverabile in molti campi d\'attività, non in letteratura. La letteratura vive solo se si pone degli obiettivi smisurati, anche al di là d\'ogni possibilità di realizzazione: solo se poeti e scrittori si proporranno imprese che nessun altro osa immaginare la letteratura continuerà ad avere una funzione. Da quando la scienza diffida delle spiegazioni generali e delle soluzioni che non siano settoriali e specialistiche, la grande sfida per la letteratura è il saper tessere insieme i diversi saperi e i diversi codici in una visione plurima, sfaccettata del mondo. (da Lezioni americane)")
        quotes.add("Sono convinto che scrivere prosa non dovrebbe essere diverso dallo scrivere poesia; in entrambi i casi è ricerca d\'un\'espressione necessaria, unica, densa, concisa, memorabile. (da Lezioni americane, \'Rapidità\')")
        quotes.add("La novella è un cavallo: un mezzo di trasporto, con una sua andata, trotto o galoppo, secondo il percorso che deve compiere, ma le velocità di cui si parla è una velocità mentale. (da Lezioni americane, p. 47)")
        quotes.add("Romanzo contemporaneo come enciclopedia [...] (Carlo Emilio Gadda) cercò per tutta la vita di rappresentare il mondo come un garbuglio, o groviglio, o gomitolo, di rappresentarlo senza attenuarne affatto l\'inestricabile complessità, o per meglio dire la presenza simultanea degli elementi più eterogenei che concorrono a determinare ogni evento. (da Lezioni americane, pp. 103-4)")
        quotes.add("L\'Insostenibile Leggerezza dell\'Essere è in realtà un\'amara constatazione dell\'Ineluttabile Pesantezza del Vivere [...] Il peso del vivere per Kundera sta in ogni forma di costrizione: la fitta rete di costrizioni pubbliche e private che finisce per avvolgere ogni esistenza con nodi sempre più stretti. Il suo romanzo ci dimostra come nella vita tutto quello che scegliamo e apprezziamo come leggero non tarda a rivelare il proprio peso insostenibile. Forse solo la vivacità e la mobilità dell\'intelligenza sfuggono a questa condanna: le qualità con cui è scritto il romanzo, che appartengono a un altro universo da quello del vivere. (da Lezioni americane, \'Leggerezza\')")
        quotes.add("Dato che in ognuna di queste conferenze mi sono proposto di raccomandare al prossimo millennio un valore che mi sta a cuore, oggi il valore che voglio raccomandare è proprio questo: in un\'epoca in cui altri \'media\' velocissimi e di estesissimo raggio trionfano, e rischiano d\'appiattire ogni comunicazione in una crosta uniforme e omogenea, la funzione della letteratura è la comunicazione tra ciò che è diverso in quanto è diverso, non ottundendone bensì esaltandone la differenza, secondo la vocazione propria del linguaggio scritto. (da Lezioni americane, \'Rapidità\')")
        quotes.add("Poi, l\'informatica. È vero che il software non potrebbe esercitare i poteri della sua leggerezza se non mediante la pesantezza del hardware; ma è il software che comanda, che agisce sul mondo esterno e sulle macchine, le quali esistono solo in funzione del software, si evolvono in modo d\'elaborare programmi sempre più complessi. La seconda rivoluzione industriale non si presenta come la prima con immagini schiaccianti quali presse di laminatoi o colate d\'acciaio, ma come i bits d\'un flusso d\'informazione che corre sui circuiti sotto forma d\'impulsi elettronici. Le macchine di ferro ci sono sempre, ma obbediscono ai bits senza peso. (da Lezioni americane, \'Leggerezza\')")
        quotes.add("Amo Gilbert Keith Chesterton perché voleva essere il Voltaire cattolico e io volevo essere il Chesterton comunista. (da Perché leggere i classici)")
        quotes.add("I classici sono quei libri di cui si sente dire di solito: «Sto rileggendo...» e mai «Sto leggendo...». (da Perché leggere i classici)")
        quotes.add("Si dicono classici quei libri che costituiscono una ricchezza per chi li ha letti e amati; ma costituiscono una ricchezza non minore per chi si riserba la fortuna di leggerli per la prima volta nelle condizioni migliori per gustarli. (da Perché leggere i classici)")
        quotes.add("I classici sono libri che esercitano un\'influenza particolare sia quando s\'impongono come indimenticabili, sia quando si nascondono nelle pieghe della memoria mimetizzandosi da inconscio collettivo o individuale. (da Perché leggere i classici)")
        quotes.add("D\'un classico ogni rilettura è una lettura di scoperta come la prima. (da Perché leggere i classici)")
        quotes.add("D\'un classico ogni prima lettura è in realtà una rilettura. (da Perché leggere i classici)")
        quotes.add("Un classico è un libro che non ha mai finito di dire quel che ha da dire. (da Perché leggere i classici)")
        quotes.add("I classici sono quei libri che ci arrivano portando su di sé la traccia delle letture che hanno preceduto la nostra e dietro di sé la traccia che hanno lasciato nella cultura o nelle culture che hanno attraversato (o più semplicemente nel linguaggio o nel costume). (da Perché leggere i classici)")
        quotes.add("Un classico è un\'opera che provoca incessantemente un pulviscolo di discorsi critici su di sé, ma continuamente se li scrolla di dosso. (da Perché leggere i classici)")
        quotes.add("I classici sono libri che quanto più si crede di conoscerli per sentito dire, tanto più quando si leggono davvero si trovano nuovi, inaspettati, inediti. (da Perché leggere i classici)")
        quotes.add("Chiamasi classico un libro che si configura come equivalente dell\'universo, al pari degli antichi talismani. (da Perché leggere i classici)")
        quotes.add("Il «tuo» classico è quello che non può esserti indifferente  e che ti serve per definire te stesso in rapporto e magari in contrasto con lui. (da Perché leggere i classici)")
        quotes.add("Un classico è un libro che viene prima di altri classici; ma chi ha letto prima gli altri e poi legge quello, riconosce subito il suo posto nella genealogia. (da Perché leggere i classici)")
        quotes.add("È classico ciò che tende a relegare l\'attualità al rango di rumore di fondo, ma nello stesso tempo di questo rumore di fondo non può fare a meno. (da Perché leggere i classici)")
        quotes.add("È classico ciò che persiste come rumore di fondo anche là dove l\'attualità più incompatibile fa da padrona. (da Perché leggere i classici)")
        quotes.add("A me – dice – piacciono i libri in cui tutti i misteri e le angosce passano attraverso una mente esatta e fredda e senza ombre come quella d\'un giocatore di scacchi. (da Se una notte d\'inverno un viaggiatore)")
        quotes.add("Anche ricordare il male può essere un piacere quando il male è mescolato non dico al bene ma al vario, al mutevole, al movimentato, insomma a quello che posso pure chiamare il bene e che è il piacere di vedere le cose a distanza e di raccontarle come ciò che è passato. (da Se una notte d\'inverno un viaggiatore) ")
        quotes.add("Ascoltare qualcuno che legge ad alta voce è molto diverso che leggere in silenzio. Quando leggi, puoi fermarti o sorvolare sulle frasi: il tempo sei tu che lo decidi. Quando è un altro che legge è difficile far coincidere la tua attenzione col tempo della sua lettura: la voce va o troppo svelta o troppo piano. (da Se una notte d\'inverno un viaggiatore)")
        quotes.add("Ci sono giorni in cui ogni cosa che vedo mi sembra carica di significati: messaggi che mi sarebbe difficile comunicare ad altri, definire, tradurre in parole, ma che appunto perciò mi si presentano come decisivi. Sono annunci o presagi che riguardano me e il mondo insieme: e di me non gli avvenimenti esteriori dell\'esistenza ma ciò che accade dentro, nel fondo; e del mondo non qualche fatto particolare ma il modo d\'essere generale di tutto. Comprenderete dunque la mia difficoltà a parlarne, se non per accenni. (da Se una notte d\'inverno un viaggiatore) ")
        quotes.add("Come stabilire il momento esatto in cui comincia una storia? Tutto è sempre cominciato già prima. La prima riga della prima pagina di ogni romanzo rimanda a qualcosa che è già successo fuori del libro. Oppure la vera storia è quella che comincia dieci pagine più avanti e tutto ciò che precede è solo un prologo. (da Se una notte d\'inverno un viaggiatore) ")
        quotes.add("I lettori sono i miei vampiri. (da Se una notte d\'inverno un viaggiatore)")
        quotes.add("Il meglio che ci si può aspettare è di evitare il peggio. (da Se una notte d\'inverno un viaggiatore) ")
        quotes.add("Lei crede che ogni storia debba avere un principio e una fine? Anticamente un racconto aveva solo due modi per finire: passate tutte le prove, l\'eroe e l\'eroina si sposavano oppure morivano. Il senso ultimo a cui rimandano tutti i racconti ha due facce: la continuità della vita, l\'inevitabilità della morte. Passaggio citato dal prof. Hilbert ad Harold Crick in\'Vero come la finzione\'.  (da Se una notte d\'inverno un viaggiatore)")
        quotes.add("L\'aspetto in cui l\'amplesso e la lettura s\'assomigliano di più è che al loro interno s\'aprono tempi e spazi diversi dal tempo e dallo spazio misurabili. (da Se una notte d\'inverno un viaggiatore)")
        quotes.add("La lettura è solitudine. Si legge da soli anche quando si è in due. (da Se una notte d\'inverno un viaggiatore) ")
        quotes.add("La lettura è un atto necessariamente individuale, molto più dello scrivere. (da Se una notte d\'inverno un viaggiatore) ")
        quotes.add("Le cose che il romanzo non dice sono necessariamente più di quelle che dice, e solo un particolare riverbero di ciò che è scritto può dare l\'illusione di stare leggendo anche il non scritto. (da Se una notte d\'inverno un viaggiatore)")
        quotes.add("Scrivere è sempre nascondere qualcosa in modo che venga poi scoperto. (da Se una notte d\'inverno un viaggiatore) ")
        quotes.add("Se una notte d\'inverno un viaggiatore, fuori dell\'abitato di Malbork, sporgendosi dalla costa scoscesa senza temere il vento e la vertigine, guarda in basso dove l\'ombra s\'addensa in una rete di linee che s\'allacciano, in una rete di linee che s\'intersecano sul tappeto di foglie illuminate dalla luna intorno a una fossa vuota – Quale storia laggiù attende la fine? – chiede, ansioso d\'ascoltare il racconto. (da Se una notte d\'inverno un viaggiatore)")
        quotes.add("Questo intendo quando dico che vorrei risalire il corso del tempo: vorrei cancellare le conseguenze di certi avvenimenti e restaurare una condizione iniziale. Ma ogni momento della mia vita porta con sé un\'accumulazione di fatti nuovi e ognuno di questi fatti nuovi porta con sé le sue conseguenze, cosicché più cerco di tornare al momento zero da cui sono partito più me ne allontano : pur essendo tutti i miei atti intesi a cancellare conseguenze d\'atti precedenti e riuscendo anche a ottenere risultati apprezzabili in questa cancellazione, devo però tener conto che ogni mia mossa per cancellare avvenimenti precedenti provoca una pioggia di nuovi avvenimenti che complicano la situazione peggio di prima e che dovrò cercare di cancellare a loro volta. (da Se una notte d\'inverno un viaggiatore)")
        quotes.add("Tutti abbiamo una ferita segreta per riscattare la quale combattiamo. (da Il sentiero dei nidi di ragno)")
        quotes.add("I grandi sono una razza ambigua e traditrice, non hanno quella serietà terribile nei giochi propria dei ragazzi, pure hanno anch \'essi i loro giochi, sempre più seri, un gioco dentro l\'altro che non si riesce mai a capire qual è il gioco vero. (da Il sentiero dei nidi di ragno)")
        quotes.add("Pin, il codice penale è sbagliato. C\'è scritto tutto quello che uno non può fare nella vita: furto, omicidio, ricettazione, appropriazione indebita, ma non c\'è scritto cosa uno può fare, invece di fare tutte quelle cose, quando si trova in certe condizioni. (da Il sentiero dei nidi di ragno)")
        quotes.add("Al principio di tutte le storie che finiscono male c\'è una donna, non si sbaglia. Tu sei giovane, impara quello che ti dico: la guerra è tutta colpa delle donne... (da Il sentiero dei nidi di ragno)")
        quotes.add("I sogni dei partigiani sono rari e corti, sogni nati dalle notti di fame, legati alla storia del cibo sempre poco e da dividere in tanti: sogni di pezzi di pane morsicati e poi chiusi in un cassetto. I cani randagi devono fare sogni simili, d\'ossa rosicchiate e nascoste sottoterra. Solo quando lo stomaco è pieno, il fuoco è acceso, e non s\'è camminato troppo durante il giorno, ci si può permettere di sognare una donna nuda e ci si sveglia al mattino sgombri e spumanti, con una letizia come d\'ancore salpate. (da Il sentiero dei nidi di ragno)")
        quotes.add("Il comunismo è che se entri in una casa e mangiano della minestra, ti diano della minestra, anche se sei stagnino, e se mangiano del panettone, a Natale, ti diano del panettone. Ecco cos\'è il comunismo. (da Il sentiero dei nidi di ragno) ")
        quotes.add("Arrivare e non aver paura, questa è la meta ultima dell\'uomo. (da Il sentiero dei nidi di ragno)")
        quotes.add("È triste essere come lui, un bambino nel mondo dei grandi, sempre un bambino, trattato dai grandi come qualcosa di divertente e di noioso; e non poter usare quelle loro cose misteriose ed eccitanti, armi e donne, non poter mai far parte dei loro giochi. Ma Pin un giorno diventerà grande, e potrà essere cattivo con tutti, vendicarsi di tutti quelli che non sono stati buoni con lui: Pin vorrebbe essere grande già adesso, o meglio, non grande, ma ammirato o temuto pur restando com\'è, essere bambino e insieme capo dei grandi, per qualche impresa meravigliosa. Ecco, Pin ora andrà via, lontano da questi posti ventosi e sconosciuti, nel suo regno, il fossato, nel suo posto magico dove fanno il nido i ragni. (da Il sentiero dei nidi di ragno) ")
        quotes.add("Ciò che i genitori m \'hanno detto d\'essere in principio. questo io sono: e nient \'altro. E nelle istruzioni dei genitori sono contenute le istruzioni dei genitori dei genitori alla loro volta tramandate di genitore in genitore in un\'interminabile catena d\'obbedienza. (da Ti con zero)")
        quotes.add("Quello che veramente ognuno di noi è ed ha, è il passato; quello che siamo e abbiamo è il catalogo delle possibilità non fallite, delle prove pronte a ripetersi. Non esiste un presente, procediamo ciechi verso il fuori e il dopo, sviluppando un programma stabilito con materiali che ci fabbrichiamo sempre uguali. Non tendiamo a nessun futuro, non c\'è niente che ci aspetta, siamo chiusi tra gli ingranaggi d\'una memoria che non prevede altro lavoro che il ricordare se stessa. (da Ti con zero)")
        quotes.add("Il rischio che abbiamo corso è stato vivere: vivere sempre. (da Ti con zero)")
    }
    public static randomQuote()
    {
        def rdm = new java.util.Random()
        return quotes[rdm.nextInt(quotes.size() - 1)]
    }

    public static String addLinebreaks(text, maxLineSize) {
        StringBuilder sb = new StringBuilder(text);
        int i = 0;
        while (i + maxLineSize < sb.length() && (i = sb.lastIndexOf(" ", i + maxLineSize)) != -1) {
            sb.replace(i, i + 1, "\n");
        }
        sb
    }
}

// DSL GROOVY PER CALVINO
public class CalvinoInterpreter
{
    static final String SCRIPT_FILENAME = 'groovysh_evaluate'
    
    private final Logger log = Logger.create(this.class)

    private final GroovyShell shell

    CalvinoInterpreter(ClassLoader classLoader, final Binding binding) {
        assert classLoader
        assert binding
        //AST MAGIC
        def astCustomizer = new org.codehaus.groovy.control.customizers.SecureASTCustomizer(
        statementsBlacklist: [  IfStatement,
                                AssertStatement,
                                BreakStatement,
                                CaseStatement,
                                CatchStatement,
                                ContinueStatement,
                                DoWhileStatement, 
                                ForStatement,
                                SwitchStatement,
                                SynchronizedStatement,
                                ThrowStatement,
                                TryCatchStatement,
                                WhileStatement]//,
        )

        // Add SecureASTCustomizer to configuration for shell.
        def conf = new CompilerConfiguration()
        conf.addCompilationCustomizers(astCustomizer)
        //END AST MAGIC 
        shell = new GroovyShell(classLoader, binding, conf)
    }

    Binding getContext() {
        return shell.getContext()
    }

    GroovyClassLoader getClassLoader() {
        return shell.getClassLoader()
    }

    def evaluate(final List buffer) {
        assert buffer

        def source = buffer.join(Parser.NEWLINE)

        def result

        Class type
        try {
            Script script = shell.parse(source, SCRIPT_FILENAME)
            type = script.getClass()

            log.debug("Compiled script: $script")

            if (type.declaredMethods.any { it.name == 'main' }) {
                result = script.run()
            }

            // Need to use String.valueOf() here to avoid icky exceptions causes by GString coercion
            log.debug("Evaluation result: ${String.valueOf(result)} (${result?.getClass()})")

            // Keep only the methods that have been defined in the script
            type.declaredMethods.each { Method m ->
                if (!(m.name in [ 'main', 'run' ] || m.name.startsWith('super$') || m.name.startsWith('class$') || m.name.startsWith('$'))) {
                    log.debug("Saving method definition: $m")

                    context["${m.name}"] = new MethodClosure(type.newInstance(), m.name)
                }
            }
        }
        finally {
            def cache = classLoader.classCache

            // Remove the script class generated
            cache.remove(type?.name)

            // Remove the inline closures from the cache as well
            cache.remove('$_run_closure')
        }

        return result
    }
}
