package me.owdding.catharsis.features.entity.models

import me.owdding.catharsis.Catharsis
import me.owdding.catharsis.utils.geometry.BedrockGeometry
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.model.geom.builders.PartDefinition
import net.minecraft.client.renderer.entity.state.EntityRenderState
import org.joml.Vector3f
import kotlin.jvm.optionals.getOrNull

class SafeModelPart(
    cubes: List<Cube>,
    children: Map<String, ModelPart>,
    initialPose: PartPose
) : ModelPart(cubes, children) {

    init {
        this.initialPose = initialPose
    }

    override fun getChild(name: String): ModelPart {
        return if (super.hasChild(name)) {
            val unsafeModelPart = super.getChild(name)

            SafeModelPart(
                unsafeModelPart.cubes,
                unsafeModelPart.children,
                unsafeModelPart.initialPose
            )
        } else {
            Catharsis.warn("model missing bone!")
            DummyModelPart()
        }
    }

    companion object {

        @JvmStatic
        fun <T : EntityRenderState> replaceModel(model: EntityModel<T>, renderState: T): EntityModel<T> {

            val newCustomEntityModel = renderState.`catharsis$getCustomEntityModel`()?.getModelPart() ?: return model

            val modelConstructor = model.javaClass.getConstructor(ModelPart::class.java)

            val newModel = modelConstructor.newInstance(newCustomEntityModel)

            return newModel
        }

        fun convertFromBedrockModel(model: BedrockGeometry): ModelPart {
            val meshDefinition = MeshDefinition()
            val root = meshDefinition.root
            val knownBones = mutableMapOf<String?, PartDefinition>()
            val knownOffsets = mutableMapOf<String?, Vector3f>()

            knownBones[null] = root
            knownOffsets[null] = Vector3f(0f, 24f, 0f)

            for (bone in model.bones) {
                val cubesBuilder = CubeListBuilder.create()

                val parentBone = knownBones[bone.parent] ?: continue
                val offsetOffset = knownOffsets[bone.parent] ?: continue
                val offset = Vector3f(offsetOffset.x - bone.pivot[0], offsetOffset.y - bone.pivot[1], offsetOffset.z - bone.pivot[2])

                for (cube in bone.cubes) {
                    if (cube.uv?.right()?.isPresent ?: true) continue
                    val uv = cube.uv.left().getOrNull() ?: continue

                    //TODO: figure out originx originy originz
                    cubesBuilder.texOffs(uv[0].toInt(), uv[1].toInt())
                        .addBox(0f, 0f, 0f, cube.size[0], cube.size[1], cube.size[2], CubeDeformation(cube.inflate ?: 0f))
                }

                knownOffsets[bone.name] = offset

                val child = parentBone.addOrReplaceChild(bone.name, cubesBuilder, PartPose.offset(offset.x, offset.y, offset.z))

                knownBones[bone.name] = child
            }

            val unsafeModelPart = root.bake(model.description.textureWidth, model.description.textureHeight)

            return SafeModelPart(
                unsafeModelPart.cubes,
                unsafeModelPart.children,
                unsafeModelPart.initialPose
            )
        }
    }
}

class DummyModelPart : ModelPart(mutableListOf(), mutableMapOf()) {
    override fun getChild(name: String): ModelPart {
        return children.getOrPut(name) {
            DummyModelPart()
        }
    }
}
