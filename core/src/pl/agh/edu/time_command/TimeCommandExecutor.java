package pl.agh.edu.time_command;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimeCommandExecutor {
    private static TimeCommandExecutor instance;
    private final HashMap<LocalDateTime, List<TimeCommand>> commands;

    private TimeCommandExecutor(){
        this.commands = new HashMap<>();
    }

    public static TimeCommandExecutor getInstance() {
        if(instance == null) return new TimeCommandExecutor();
        return instance;
    }

    public boolean addCommand(LocalDateTime dateTime, TimeCommand timeCommand) {
        if (!commands.containsKey(dateTime)) {
            commands.put(dateTime, new ArrayList<>());
        }
        return commands.get(dateTime).add(timeCommand);
    }

    public void executeCommands(LocalDateTime dateTime){
        if(commands.containsKey(dateTime)){
            commands.remove(dateTime).forEach(TimeCommand::execute);
        }
    }
}
