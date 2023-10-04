package engineTester;

import Entities.*;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import renderEngine.EntityRenderer;
import shaders.StaticShader;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {
    public static void main(String[] args) {
        Random random = new Random();

        Mouse.setGrabbed(true);

        DisplayManager.createDisplay();
        Loader loader = new Loader();


        //********TERRAIN TEXTURE STUFF********

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        //*************************************


        ModelData data = OBJFileLoader.loadOBJ("stall");
        RawModel model = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
        ModelTexture texture = new ModelTexture(loader.loadTexture("stallTexture"));
        texture.setShineDamper(10);
        texture.setReflectivity(1);
        TexturedModel staticModel = new TexturedModel(model, texture);

        ModelData grassData = OBJFileLoader.loadOBJ("grassModel");
        RawModel grassModel = loader.loadToVAO(grassData.getVertices(), grassData.getTextureCoords(), grassData.getNormals(), grassData.getIndices());
        ModelTexture grassTexture = new ModelTexture(loader.loadTexture("grassTexture"));
        grassTexture.setShineDamper(10);
        grassTexture.setReflectivity(1);
        TexturedModel grassStaticModel = new TexturedModel(grassModel, grassTexture);
        grassStaticModel.getTexture().setHasTransparency(true);
        grassStaticModel.getTexture().setUseFakeLighting(true);

        Light light = new Light(new Vector3f(10, 1000, 10), new Vector3f(1, 1, 1));

        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");
        //Terrain terrain2 = new Terrain(-1, -1, loader, texturePack, blendMap, "heightmap");

        Entity entity = new Entity(staticModel, new Vector3f(50, terrain.getHeightOfTerrain(50, -50), -50), 0, 180, 0, 1);


        MasterRenderer renderer = new MasterRenderer();

        ModelData playerModelData = OBJFileLoader.loadOBJ("grassModel");
        RawModel rawPlayerModel = loader.loadToVAO(playerModelData.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
        TexturedModel playerModel = new TexturedModel(rawPlayerModel, new ModelTexture(loader.loadTexture("skin")));


        ModelData fernModelData = OBJFileLoader.loadOBJ("fern");
        RawModel rawFernModel = loader.loadToVAO(fernModelData.getVertices(), fernModelData.getTextureCoords(), fernModelData.getNormals(), fernModelData.getIndices());
        ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fern"));
        fernTexture.setNumberOfRows(2);
        fernTexture.setHasTransparency(true);
        TexturedModel fernModel = new TexturedModel(rawFernModel, fernTexture);



        Player player = new Player(playerModel, new Vector3f(100, 0, -50), 0, 0, 0, 1);

        Camera camera = new Camera(player);


        List<Entity> entities = new ArrayList<Entity>();

        List<GuiTexture> guis = new ArrayList<GuiTexture>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("health"), new Vector2f(-0.70f, -0.9f), new Vector2f(0.55f, 0.15f));
        guis.add(gui);

        GuiRenderer guiRenderer = new GuiRenderer(loader);


        for(int j = 0; j < 500; j++) {
            float x = random.nextFloat() * 800 - 400;
            float z = random.nextFloat() * -600;
            float y = terrain.getHeightOfTerrain(x, z);
            if (terrain.getHeightOfTerrain(x, z) > -30) {
                entities.add(new Entity(fernModel, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, random.nextFloat() * 0.1f + 0.6f));
            }
        }


        while(!Display.isCloseRequested()){
            player.move(terrain);
            camera.move();
            renderer.processEntity(player);
            renderer.processTerrain(terrain);
            //renderer.processTerrain(terrain2);
            renderer.processEntity(entity);

            for (Entity en : entities){
                renderer.processEntity(en);
            }

            renderer.render(light, camera);

            guiRenderer.render(guis);

            DisplayManager.updateDisplay();

            if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
                DisplayManager.closeDisplay();
            }
        }
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
