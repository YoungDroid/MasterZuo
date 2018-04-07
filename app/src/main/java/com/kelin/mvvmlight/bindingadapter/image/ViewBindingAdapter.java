package com.kelin.mvvmlight.bindingadapter.image;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.kennyc.view.MultiStateView;
import com.oom.masterzuo.R;
import com.oom.masterzuo.utils.DensityUtil;
import com.oom.masterzuo.utils.ImageViewUtils;
import com.oom.masterzuo.utils.LocalDisplay;
import com.oom.masterzuo.widget.glide.GlideRoundTransform;
import com.oom.masterzuo.widget.image.RoundImageView;

import static com.oom.masterzuo.app.MasterZuoApplication.Debug;

/**
 * Created by kelin on 16-3-24.
 */
public final class ViewBindingAdapter {

    @BindingAdapter({"uri"})
    public static void setImageUri(SimpleDraweeView simpleDraweeView, String uri) {
        if (!TextUtils.isEmpty(uri)) {
            simpleDraweeView.setImageURI(Uri.parse(uri));
        }
    }

    @BindingAdapter({"load_image_url"})
    public static void loadImageForGlide(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }


    @BindingAdapter({"imageUri"})
    public static void loadImageFromNetWork(SimpleDraweeView simpleDraweeView, Uri uri) {
        if (null != uri) {
            int resizeWidth =
                    simpleDraweeView.getLayoutParams().width > 0 ? simpleDraweeView.getLayoutParams().width >> 1 : LocalDisplay.SCREEN_WIDTH_PIXELS;
            int resizeHeight = simpleDraweeView.getLayoutParams().height > 0 ?
                    simpleDraweeView.getLayoutParams().height >> 1 : LocalDisplay.SCREEN_HEIGHT_PIXELS;

            ImageRequest request =
                    ImageRequestBuilder.newBuilderWithSource(uri).setResizeOptions(new ResizeOptions(resizeWidth, resizeHeight)).setProgressiveRenderingEnabled(true).build();


            DraweeController controller =
                    Fresco.newDraweeControllerBuilder().setImageRequest(request).setOldController(simpleDraweeView.getController()).setControllerListener(new BaseControllerListener<>()).build();
            simpleDraweeView.setController(controller);
        }
    }

    @BindingAdapter(value = {
            "uri", "placeholderImageRes", "request_width", "request_height", "onSuccessCommand", "onFailureCommand"
    }, requireAll = false)
    public static void loadImage(final ImageView imageView, String uri, @DrawableRes int placeholderImageRes, int width, int height, final ReplyCommand<Bitmap> onSuccessCommand, final ReplyCommand<DataSource<CloseableReference<CloseableImage>>> onFailureCommand) {
        imageView.setImageResource(placeholderImageRes);
        if (!TextUtils.isEmpty(uri)) {
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri));
            if (width > 0 && height > 0) {
                builder.setResizeOptions(new ResizeOptions(width, height));
            }
            ImageRequest request = builder.build();
            DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(request, imageView.getContext());
            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                @Override
                protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                    if (onFailureCommand != null) {
                        onFailureCommand.execute(dataSource);
                    }
                }

                @Override
                protected void onNewResultImpl(Bitmap bitmap) {
                    imageView.setImageBitmap(bitmap);
                    if (onSuccessCommand != null) {
                        onSuccessCommand.execute(bitmap);
                    }
                }
            }, UiThreadImmediateExecutorService.getInstance());
        }
    }

    @BindingAdapter({"autoScaleXY"})
    public static void autoScaleXY(SimpleDraweeView simpleDraweeView, Uri uri) {
        simpleDraweeView.setController(ImageViewUtils.simpleDraweeViewAutoScaleXY(simpleDraweeView, uri));
    }

    @BindingAdapter(value = {"progressAutoScaleXY", "onResultCommand"}, requireAll = false)
    public static void progressAutoScaleXY(SimpleDraweeView simpleDraweeView, Uri uri, ReplyCommand<Integer> onResultCommand) {
        if (uri == null) {
            return;
        }

        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                if (imageInfo == null) {
                    if (onResultCommand != null) {
                        onResultCommand.execute(MultiStateView.VIEW_STATE_EMPTY);
                    }
                    return;
                }

                QualityInfo qualityInfo = imageInfo.getQualityInfo();

                simpleDraweeView.setAspectRatio((float) imageInfo.getWidth() / (float) imageInfo.getHeight());

                if (onResultCommand != null) {
                    onResultCommand.execute(MultiStateView.VIEW_STATE_CONTENT);
                }
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                if (onResultCommand != null) {
                    onResultCommand.execute(MultiStateView.VIEW_STATE_EMPTY);
                }
            }
        };

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).build();

        DraweeController controller =
                Fresco.newDraweeControllerBuilder().setImageRequest(request).setOldController(simpleDraweeView.getController()).setControllerListener(controllerListener).build();
        simpleDraweeView.setController(controller);
    }


    @BindingAdapter(value = {"progressAutoScaleXY", "onResultCommand"}, requireAll = false)
    public static void progressAutoScaleXY(ImageView imageView, Uri uri, ReplyCommand<Integer> onResultCommand) {
        if (uri == null && onResultCommand != null) {
            onResultCommand.execute(MultiStateView.VIEW_STATE_EMPTY);
            return;
        }

        Glide.with(imageView.getContext()).asBitmap().load(uri).apply(new RequestOptions().centerCrop())
                .into(imageView);

        if (onResultCommand != null) {
            onResultCommand.execute(MultiStateView.VIEW_STATE_CONTENT);
        }
    }


    @BindingAdapter(value = {"autoScaleXY", "roundCorner"}, requireAll = false)
    public static void autoScaleXY(ImageView imageView, Uri uri, int roundCorner) {
        if (uri == null) {
            return;
        }

        if (roundCorner > 0) {
            Glide.with(imageView.getContext()).asBitmap().load(uri).apply(new RequestOptions().placeholder(R.mipmap.icon_app_default)
                    .transform(new GlideRoundTransform(imageView.getContext(), roundCorner)))
                    .transition(new BitmapTransitionOptions().crossFade())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            int width = (imageView.getLayoutParams().width < 0) ? LocalDisplay.SCREEN_WIDTH_PIXELS : imageView.getLayoutParams().width;
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            imageView.getLayoutParams().height = (int) (resource.getHeight() * (width / (float) resource.getWidth()));
                            imageView.setImageBitmap(resource);
                        }
                    });
        } else {

            Glide.with(imageView.getContext()).asBitmap().load(uri).apply(new RequestOptions().placeholder(R.mipmap.icon_app_default))
                    .transition(new BitmapTransitionOptions().crossFade())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            int width = (imageView.getLayoutParams().width < 0) ? LocalDisplay.SCREEN_WIDTH_PIXELS : imageView.getLayoutParams().width;
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            imageView.getLayoutParams().height = (int) (resource.getHeight() * (width / (float) resource.getWidth()));
                            imageView.setImageBitmap(resource);
                        }
                    });
        }
    }

    @BindingAdapter(value = {"imageUriWithXY", "setX", "setY"})
    public static void imageSetXY(ImageView imageView, Uri uri, int setX, int setY) {
        if (uri == null || setX < 0 || setY < 0) {
            return;
        }

        Glide.with(imageView.getContext()).asBitmap().load(uri).apply(new RequestOptions().placeholder(R.mipmap.icon_app_default))
                .transition(new BitmapTransitionOptions().crossFade())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageView.getLayoutParams().width = LocalDisplay.dp2px(setX);
                        imageView.getLayoutParams().height = LocalDisplay.dp2px(setY);
                        imageView.setImageBitmap(resource);
                    }
                });
    }

    @BindingAdapter(value = {"imageUriByGlide", "roundCorner"}, requireAll = false)
    public static void setImageUri(ImageView imageView, Uri uri, int roundCorner) {
        if (uri == null) {
            return;
        }

        Glide.with(imageView.getContext())
                .load(uri)
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.icon_app_default)
                        .transform(new GlideRoundTransform(imageView.getContext(), roundCorner)))
                .into(imageView);
    }

    @BindingAdapter({"imageResourceByGlide"})
    public static void setImageResource(ImageView imageView, int resourceID) {
        if (0 == resourceID) {
            return;
        }

        Glide.with(imageView.getContext())
                .load(resourceID)
                .apply(new RequestOptions().centerCrop()
                        .transform(new GlideRoundTransform(imageView.getContext())))
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    @BindingAdapter({"imageUriByGlide", "viewAspectRatio"})
    public static void setImageUri(ImageView imageView, Uri uri, float viewAspectRatio) {
        if (uri == null) {
            return;
        }

        Glide.with(imageView.getContext())
                .asBitmap()
                .load(uri)
                .apply(new RequestOptions().transform(new GlideRoundTransform(imageView.getContext())))
                .transition(new BitmapTransitionOptions().crossFade())
                .into(imageView);
    }

    @BindingAdapter({"imageUriWithXY", "setX", "setY"})
    public static void compatImageSetXY(RoundImageView imageView, Uri uri, int setX, int setY) {

        if (null == uri || setX <= 0 || setY <= 0) {
            return;
        }

        int actualWidth = setX;
        int actualHeight = setY;
        if (actualWidth > 300) {
            actualWidth = setY > setX ? 180 : 300;
        }
        if (actualHeight > 240) {
            actualHeight = 240;
        }

        if (Debug) {
            Log.e("ViewBindingAdapter", "compatImageSetXY: " + actualWidth + "\t" + actualHeight);
        }
        imageView.setParams(DensityUtil.px2dip(imageView.getContext(), (float) (
                actualWidth * 1.8
        )), DensityUtil.px2dip(imageView.getContext(), (float) (actualHeight * 1.8)));

        Glide.with(imageView.getContext()).load(uri)
                .transition(new DrawableTransitionOptions().crossFade())
                .apply(new RequestOptions().fitCenter()).into(imageView);
    }
}

