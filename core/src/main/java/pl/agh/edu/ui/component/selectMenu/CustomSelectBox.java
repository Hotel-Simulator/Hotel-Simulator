package pl.agh.edu.ui.component.selectMenu;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.component.label.CustomLabel;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
import pl.agh.edu.utils.LanguageString;

import java.util.function.Function;

import static com.badlogic.gdx.utils.Align.center;
import static pl.agh.edu.ui.audio.SoundAudio.CLICK;
import static pl.agh.edu.ui.utils.SkinFont.SUBTITLE1;

public class CustomSelectBox extends WrapperTable {
    private final Array<SelectMenuItem> items;
    private final SelectBox<SelectMenuItem> selectOption = new CustomSelectBox.DropDownSelect();

    public CustomSelectBox(Array<SelectMenuItem> items, Function<? super SelectMenuItem, Void> function) {
        super();
        this.items = items;

        setMaxListCount();
        setListItems(items);
        setFunction(function);

        innerTable.add(selectOption).pad(0f).growX().uniform().minHeight(0f);

        this.setResolutionChangeHandler(this::changeResolutionHandler);
    }

    public void setItem(String name) {
        for (SelectMenuItem selectMenuItem : items) {
            if (selectMenuItem.name.equals(name)) {
                this.selectOption.setSelected(selectMenuItem);
                return;
            }
        }
    }

    private void setMaxListCount() {
        switch (GraphicConfig.getResolution().SIZE) {
            case SMALL -> selectOption.setMaxListCount(3);
            case MEDIUM -> selectOption.setMaxListCount(5);
            case LARGE -> selectOption.setMaxListCount(7);
        }
    }

    public void setListItems(Array<SelectMenuItem> items) {
        selectOption.setItems(items);
    }

    public void setFunction(Function<? super SelectMenuItem, Void> function) {
        selectOption.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                function.apply(items.get(selectOption.getSelectedIndex()));
            }
        });
    }

    private void changeResolutionHandler() {
        this.size(CustomSelectBox.SelectMenuStyle.getWidth(), CustomSelectBox.SelectMenuStyle.getHeight());
        this.validate();
    }

    private static class SelectMenuStyle {
        public static float getHeight() {
            return switch (GraphicConfig.getResolution().SIZE) {
                case SMALL -> 30f + 2 * getPadding();
                case MEDIUM -> 35f + 2 * getPadding();
                case LARGE -> 40f + 2 * getPadding();
            };
        }

        public static float getWidth() {
            return switch (GraphicConfig.getResolution().SIZE) {
                case SMALL -> 150f;
                case MEDIUM -> 200f;
                case LARGE -> 300f;
            };
        }

        public static float getPadding() {
            return switch (GraphicConfig.getResolution().SIZE) {
                case SMALL -> 5f;
                case MEDIUM -> 10f;
                case LARGE -> 15f;
            };
        }
    }

    private class DropDownSelect extends SelectBox<SelectMenuItem> {
        public DropDownSelect() {
            super(skin.get("CustomSelectBox", SelectBox.SelectBoxStyle.class));
            setUpSelectionPane();
            this.getList().setAlignment(center);
        }

        private void setUpSelectionPane() {
            List.ListStyle listStyle = this.getList().getStyle();

            listStyle.selection.setBottomHeight(CustomSelectBox.SelectMenuStyle.getPadding());
            listStyle.selection.setTopHeight(CustomSelectBox.SelectMenuStyle.getPadding());

            this.getList().setStyle(listStyle);
        }

        @Override
        public void validate() {
            setHeight(CustomSelectBox.SelectMenuStyle.getHeight());
            this.layout();
        }

        @Override
        protected GlyphLayout drawItem(Batch batch, BitmapFont font, SelectMenuItem item, float x, float y, float width) {
            String string = this.getSelected().toString();
            return font.draw(batch, string, this.getX(), this.getY() + (this.getHeight() + font.getXHeight()) / 2, 0, string.length(), this.getWidth(), center, false, "...");
        }

        @Override
        protected void onShow(Actor scrollPane, boolean below) {
            super.onShow(scrollPane, below);
            CLICK.playSound();
        }

        @Override
        protected void onHide(Actor scrollPane) {
            super.onHide(scrollPane);
            CLICK.playSound();
        }
    }
}
