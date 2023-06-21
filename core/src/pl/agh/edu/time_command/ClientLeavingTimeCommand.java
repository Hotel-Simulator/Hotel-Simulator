package pl.agh.edu.time_command;

import pl.agh.edu.model.ClientGroup;

public class ClientLeavingTimeCommand implements TimeCommand {

    private final ClientGroup clientGroup;

    public ClientLeavingTimeCommand(ClientGroup clientGroup) {
        this.clientGroup = clientGroup;
    }

    @Override
    public void execute() {
//        clientGroup.getRoom().setState();
        // TODO: 19.06.2023 klienci zwalniają pokój (i zostawiają recenzje)
    }
}
