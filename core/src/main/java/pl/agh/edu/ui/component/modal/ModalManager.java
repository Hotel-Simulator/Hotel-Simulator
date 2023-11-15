package pl.agh.edu.ui.component.modal;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import java.util.ArrayList;
import java.util.List;
import pl.agh.edu.engine.event.EventModalData;
import pl.agh.edu.ui.component.modal.event.EventWrapper;
import pl.agh.edu.ui.component.modal.options.OptionsWrapper;
import pl.agh.edu.ui.shader.BlurShader;

public class ModalManager extends Stack{
    private static ModalManager instance;
    private static InputMultiplexer inputMultiplexer;
    private static BlurShader blurShader;
    private static Stage mainStage;
    private static Stage eventStage;
    private final List<BaseModalWrapper> modalList = new ArrayList<>();
    private ModalManager(
            InputMultiplexer inputMultiplexer,
            BlurShader blurShader,
            Stage mainStage,
            Stage eventStage) {
        super();
        this.setFillParent(true);
        ModalManager.inputMultiplexer = inputMultiplexer;
        ModalManager.blurShader = blurShader;
        ModalManager.mainStage = mainStage;
        ModalManager.eventStage = eventStage;
    }
    public static ModalManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ModalManager not initialized. Call initialize first.");
        }
        return instance;
    }

    public static ModalManager initialize(InputMultiplexer inputMultiplexer,
                                  BlurShader blurShader,
                                  Stage mainStage,
                                  Stage eventStage
    ) {
        instance = new ModalManager(inputMultiplexer, blurShader, mainStage, eventStage);
        return instance;
    }

    private void addModal(BaseModalWrapper actor){
        actor.openModal();
        modalList.add(0,actor);
        this.add(actor);
    }
    public void setUpOptionModal(){
        addModal(new OptionsWrapper(inputMultiplexer, blurShader, mainStage, eventStage));
    }
    public void showEventModal(EventModalData eventModalData){
        addModal(new EventWrapper(inputMultiplexer, blurShader, mainStage, eventStage, eventModalData));
    }
    public void closeModal(){
        BaseModalWrapper currentModal = modalList.get(0);
        modalList.remove(0);
        this.removeActor(currentModal);
        currentModal.closeModal();
        if(!modalList.isEmpty()){
            BaseModalWrapper newModal = modalList.get(0);
            newModal.openModal();
        }
    }
    
    public boolean isModalActive(){
        return this.getChildren().size>0;
    }

    public boolean isModalReadyToClose(){
        return this.getChildren().size<=1;
    }
}
