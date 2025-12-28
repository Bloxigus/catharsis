package me.owdding.catharsis.utils.geometry

import me.owdding.catharsis.Catharsis
import me.owdding.catharsis.utils.extensions.toVector3f
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.model.geom.builders.PartDefinition
import org.joml.Vector3f
import org.joml.plus
import java.util.function.Function
import kotlin.jvm.optionals.getOrNull

class SafeModelPart(
    cubes: List<Cube>,
    children: Map<String, ModelPart>,
    initialPose: PartPose,
    val name: String = "Unnamed",
    val path: String = "root"
) : ModelPart(cubes, children) {

    init {
        this.initialPose = initialPose
        this.loadPose(initialPose)
    }

    override fun getChild(name: String): ModelPart {
        return if (super.hasChild(name)) {
            val unsafeModelPart = super.getChild(name)

            SafeModelPart(
                unsafeModelPart.cubes,
                unsafeModelPart.children,
                unsafeModelPart.initialPose,
                this.name,
                "$path/$name"
            )
        } else {
            Catharsis.warn("model ${this.name} missing bone $name!")
            DummyModelPart()
        }
    }

    override fun createPartLookup(): Function<String, ModelPart?> {
        val parentLookup = super.createPartLookup()
        return Function { boneName: String ->
            val parentBone = parentLookup.apply(boneName)
            if (parentBone == null) {
                Catharsis.warn("Bone $path in model $name is missing!")
                DummyModelPart()
            } else parentBone
        }
    }

    companion object {
        fun convertFromBedrockModel(model: BedrockGeometry?): ModelPart? {
            if (model == null) return null

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
                val pivot = bone.pivot.toVector3f()
                val offset = Vector3f(
                    offsetOffset.x - pivot.x,
                    offsetOffset.y - pivot.y,
                    pivot.z - offsetOffset.z
                )

                for (cube in bone.cubes) {
                    if (cube.uv?.right()?.isPresent ?: true) continue
                    val uv = cube.uv.left().getOrNull() ?: continue
                    val size = cube.size.toVector3f()
                    val origin = cube.origin.toVector3f()
                    val to = origin + size

                    val cubeOrigin = Vector3f(
                        pivot.x - to.x,
                        pivot.y - to.y,
                        origin.z - pivot.z
                    )

                    cubesBuilder
                        .texOffs(uv[0].toInt(), uv[1].toInt())
                        .addBox(
                            cubeOrigin.x, cubeOrigin.y, cubeOrigin.z,
                            size.x, size.y, size.z,
                            CubeDeformation(cube.inflate ?: 0f)
                        )
                }

                knownOffsets[bone.name] = pivot

                val child = parentBone.addOrReplaceChild(bone.name, cubesBuilder, PartPose.offset(offset.x, offset.y, offset.z))

                knownBones[bone.name] = child
            }

            val unsafeModelPart = root.bake(model.description.textureWidth, model.description.textureHeight)

            return SafeModelPart(
                unsafeModelPart.cubes,
                unsafeModelPart.children,
                unsafeModelPart.initialPose,
                name = model.description.identifier
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

    override fun createPartLookup(): Function<String, ModelPart?> {
        return Function {
            DummyModelPart()
        }
    }
}
