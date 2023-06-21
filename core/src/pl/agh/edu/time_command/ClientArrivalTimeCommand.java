package pl.agh.edu.time_command;

import org.json.simple.parser.ParseException;
import pl.agh.edu.model.ClientGroup;
import pl.agh.edu.model.Hotel;

import java.io.IOException;

public class ClientArrivalTimeCommand implements TimeCommand {

    private final ClientGroup clientGroup;

    private final TimeCommandExecutor timeCommandExecutor;

    public ClientArrivalTimeCommand(ClientGroup clientGroup) throws IOException, ParseException {
        this.clientGroup = clientGroup;
        this.timeCommandExecutor = TimeCommandExecutor.getInstance();
    }

    // TODO: 19.06.2023 dla danej grupy sprawdzamy czy jest odpowiedni pokój i jeśli tak to go zajmujemy i dodajemy komende ClientLeaving
    @Override
    public void execute() {

    }
}
