package engine.component;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class MeshComponent extends Component {
    public Vector2 vector;

    public MeshComponent(Vector2 vector) {
        this.vector = vector;
    }

}
