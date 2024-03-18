package es.degrassi.forge.core.client;

import com.google.gson.JsonObject;
import es.degrassi.forge.core.common.machines.block.SolarPanelBlock;
import es.degrassi.forge.core.init.BlockRegistration;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BooleanSupplier;
import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.util.shaded.json.JSONObject;

public class DegrassiResourcePack implements PackResources {
  public final Map<ResourceLocation, IResourceStreamSupplier> resourceMap = new HashMap<>();

  private static IResourceStreamSupplier ofText(String text) {
    return IResourceStreamSupplier.create(() -> true, () -> new ByteArrayInputStream(text.getBytes()));
  }

  private static IResourceStreamSupplier ofFile(File file) {
    return IResourceStreamSupplier.create(file::isFile, () -> new FileInputStream(file));
  }

  private static IResourceStreamSupplier oneOfFiles(File... file) {
    return IResourceStreamSupplier.create(
      () -> Arrays.stream(file).anyMatch(File::isFile),
      () -> new FileInputStream(Arrays.stream(file).filter(File::isFile).findFirst().orElseThrow(FileNotFoundException::new))
    );
  }

  static DegrassiResourcePack packInstance;
  public static DegrassiResourcePack getPackInstance() {
    if (packInstance == null)
      packInstance = new DegrassiResourcePack();
    return packInstance;
  }

  private boolean hasInit = false;

  public void init () {
    if (hasInit) return;
    hasInit = true;
    resourceMap.clear();

    BlockRegistration.listPanels().forEach(si -> {
      SolarPanelBlock blk = si.get();
      ResourceLocation reg = ForgeRegistries.BLOCKS.getKey(blk);

      ResourceLocation blockState = new ResourceLocation(reg.getNamespace(), "blockstates/" + reg.getPath() + ".json");
      ResourceLocation models_block = new ResourceLocation(reg.getNamespace(), "models/block/" + reg.getPath() + ".json");
      ResourceLocation models_item = new ResourceLocation(reg.getNamespace(), "models/item/" + reg.getPath() + ".json");

      resourceMap.put(blockState, ofText("{\"variants\":{\"\":{\"model\":\"" + reg.getNamespace() + ":block/" + reg.getPath() + "\"}}}"));
      resourceMap.put(models_item, ofText("{\"parent\":\"" + reg.getNamespace() + ":block/" + reg.getPath() + "\"}"));

      // Block model
      var blockModel = new JSONObject();
      blockModel.put("loader", "degrassi:sp");
      blockModel.put("panel", reg.toString());
      resourceMap.put(models_block, ofText(blockModel.toString()));
    });
  }

  @Nullable
  @Override
  public IoSupplier<InputStream> getRootResource(String... elements) {
    return null;
  }

  @Nullable
  @Override
  public IoSupplier<InputStream> getResource(PackType packType, ResourceLocation location) {
    var res = resourceMap.get(location);
    if (res == null) return null;

    if (!res.exists()) return null;

    return () -> {
      try {
        init();
        return res.create();
      } catch (RuntimeException e) {
        if (e.getCause() instanceof IOException)
          throw (IOException) e.getCause();
        throw e;
      }
    };
  }

  @Override
  public void listResources(PackType packType, String namespace, String path, ResourceOutput resourceOutput) {
    if (!namespace.equals("degrassi")) return;
    init();

    for (var e : resourceMap.entrySet())
      if (e.getKey().getPath().startsWith(path) && e.getValue().exists())
        resourceOutput.accept(e.getKey(), e.getValue()::create);
  }

  @Override
  public Set<String> getNamespaces(PackType type) {
    init();
    return Collections.singleton("degrassi");
  }

  @Nullable
  @Override
  public <T> T getMetadataSection(MetadataSectionSerializer<T> deserializer) throws IOException {
    if (deserializer.getMetadataSectionName().equals("pack")) {
      JsonObject obj = new JsonObject();
      obj.addProperty("pack_format", SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES));
      obj.addProperty("description", "Generated resources for Degrassi");
      return deserializer.fromJson(obj);
    }
    return null;
  }

  @Override
  public String packId() {
    return "Degrassi";
  }

  @Override
  public void close() {

  }

  @Override
  public boolean isHidden() {
    return true;
  }

  public interface IResourceStreamSupplier {
    static IResourceStreamSupplier create(BooleanSupplier exists, IIOSupplier<InputStream> streamable) {
      return new IResourceStreamSupplier() {
        @Override
        public boolean exists() {
          return exists.getAsBoolean();
        }

        @Override
        public InputStream create() throws IOException {
          return streamable.get();
        }
      };
    }

    boolean exists();
    InputStream create() throws IOException;
  }

  @FunctionalInterface
  public interface IIOSupplier<T> {
    T get() throws IOException;
  }
}
