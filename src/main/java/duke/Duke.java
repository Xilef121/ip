package duke;

import duke.command.Command;
import duke.task.TaskCommandPair;
import duke.task.TaskList;

import java.nio.file.Paths;
import java.util.EmptyStackException;
import java.util.Stack;

public class Duke {
    private static boolean isExit;
    private final Storage storage;
    private static Stack<TaskCommandPair> temp;
    private TaskList tasks;
    private final Ui ui;
    private static final String FILE_NAME = "duke.txt";
    private static final String FILE_PATH = Paths.get(".", FILE_NAME).toString();

    public Duke() {
        ui = new Ui();
        storage = new Storage(FILE_PATH);
        temp = new Stack<TaskCommandPair>();
        try {
            tasks = new TaskList(storage.load());
        } catch (DukeException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    public String getResponse(String input) {
        String s = "";
        try {
            Command c = Parser.parse(input);
            isExit = c.isExit();
            s = c.execute(tasks, ui, storage);
        } catch (DukeException e) {
            return ui.showError(e);
        }
        return s;
    }

    public String welcome() {
        return Ui.showWelcome();
    }

    public boolean isExit() {
        return isExit;
    }

    public static void push(TaskCommandPair task) {
        temp.push(task);
    }

    public static TaskCommandPair pop() throws DukeException{
        try {
            return temp.pop();
        } catch (EmptyStackException e) {
            throw new DukeException("There is no previous actions to be undone");
        }
    }
}
