package pl.agh.edu.actor.component.table;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.component.rating.Star;
import pl.agh.edu.actor.frame.BaseFrame;
import pl.agh.edu.actor.utils.WrapperTable;

import java.util.stream.IntStream;
import java.util.List;


public abstract class BaseTable extends WrapperTable {
    private final int noColumns;
    private final float separatorWidth;
//    private final float cellWidth;
    private final float cellPadding = 10f;
    protected final float rowSpacing = 20f;
    public BaseTable(List<String> columnNames) {
        // root is for setting width of table. There is no other way to set it other than growX() while adding to frame outside of class
        super();

        this.noColumns = columnNames.size();
        this.separatorWidth = 2f;

        float framePadding = 40f;
//        cellWidth = Math.round((rootWidth - 2* framePadding - 2*noColumns*cellPadding - (noColumns-1)*separatorWidth)/noColumns)  ; // evenly distribute cells

        align(Align.bottomLeft);

        BaseRow header = createRow(columnNames.stream().map(s -> new Label(s,HotelSkin.getInstance(),"body1")).toArray(Actor[]::new));
        header.setBackground("table-header-background");
        header.align(Align.bottomLeft);
        innerTable.add(header).space(rowSpacing).growX();

    }

    public final BaseRow createRow(Actor... actors){
        BaseRow baseRow = new BaseRow();
        IntStream.range(0,noColumns).forEach(i ->{
            Actor actor = actors[i];
            Container<Actor> container = new Container<>(actor);
            container.pad(1f);
            baseRow.innerTable.add(container).growX().uniform().height(Star.getSize()+20f).center().padLeft(cellPadding).padRight(cellPadding);
            if (i != noColumns - 1)
                baseRow.innerTable.add(new Image(HotelSkin.getInstance().getPatch("table-separator-line"))).width(separatorWidth).growY().center();
        });

        baseRow.setBackground("table-row-background");
        baseRow.setDebug(true);
        innerTable.row();
        return baseRow;
    }


    //don't remove it
    public static class BaseRow extends WrapperTable {
        BaseRow() {
            super();
        }
    }

}
