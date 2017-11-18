package views

import javafx.animation.Animation
import javafx.animation.FadeTransition
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.EventHandler
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.util.Duration
import lt.neworld.climbtimer.utils.FxProperty

/**
 * @author Andrius Semionovas
 * @since 2017-11-18
 */
class ImageSliderView : Pane() {
    /**
     * Between animations
     */
    var interval: Duration = Duration.millis(3000.0)
        set(value) {
            field = value
            refreshAnimation()
        }

    /**
     * During animation
     */
    var duration: Duration = Duration.millis(300.0)
        set(value) {
            field = value
            refreshAnimation()
        }

    var images: List<Image> = emptyList()
        set(value) {
            field = value
            refreshImages()
            refreshAnimation()
        }

    private fun createImage(): ImageView {
        return ImageView().apply {
            isPreserveRatio = true
            isSmooth = true
        }
    }

    private var currentImage = createImage()
    private var nextImage = createImage().apply {
        opacity = 0.0
        fitWidthProperty().bind(currentImage.fitWidthProperty())
        fitHeightProperty().bind(currentImage.fitHeightProperty())
    }

    var fitWidth: Double by FxProperty(currentImage.fitWidthProperty().asObject())
    var fitHeight: Double by FxProperty(currentImage.fitHeightProperty().asObject())

    private var currentImageIndex = 0

    init {
        children.add(currentImage)
        children.add(nextImage)

        sceneProperty().addListener { observable, _, _ ->
            if (observable.value != null) {
                timeline?.play()
            } else {
                timeline?.stop()
            }
        }
    }

    private fun refreshImages() {
        currentImage.image = images.firstOrNull()
        nextImage.image = null
    }

    private var timeline: Timeline? = null

    private fun refreshAnimation() {
        timeline?.stop()
        imageTransitionAnimation?.stop()

        timeline = Timeline(KeyFrame(interval, "Next image", EventHandler { showNextImage() })).apply {
            cycleCount = Timeline.INDEFINITE
        }
        if (scene != null) {
            timeline!!.play()
        }
    }

    private var imageTransitionAnimation: Animation? = null

    private fun showNextImage() {
        if (images.isEmpty()) return

        currentImageIndex = ++currentImageIndex % images.size
        nextImage.image = images[currentImageIndex]

        imageTransitionAnimation = FadeTransition(duration, nextImage).apply {
            fromValue = 0.0
            toValue = 1.0
            setOnFinished {
                currentImage.image = nextImage.image
                nextImage.opacity = 0.0
            }
            play()
        }
    }
}