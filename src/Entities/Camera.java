package Entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
    private Vector3f position = new Vector3f(100, 5, -30);
    private float pitch;
    private float yaw;
    private float roll;

    public Camera(){}

    public void move(){
        float speed = 0.05f;

        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            speed = 0.5f;
        } else{
            speed = 0.05f;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            position.z -= speed;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            position.x += speed;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            position.x -= speed;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            position.z += speed;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            position.y += 0.05f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
            position.y -= 0.05f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_E)){
            yaw += 0.5f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
            yaw -= 0.5f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)){
            pitch += 0.5f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)){
            pitch -= 0.5f;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
