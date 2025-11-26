package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the {@code libs} extension.
 */
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final AirbnbLibraryAccessors laccForAirbnbLibraryAccessors = new AirbnbLibraryAccessors(owner);
    private final AndroidxLibraryAccessors laccForAndroidxLibraryAccessors = new AndroidxLibraryAccessors(owner);
    private final ApplovinLibraryAccessors laccForApplovinLibraryAccessors = new ApplovinLibraryAccessors(owner);
    private final AudienceLibraryAccessors laccForAudienceLibraryAccessors = new AudienceLibraryAccessors(owner);
    private final BumptechLibraryAccessors laccForBumptechLibraryAccessors = new BumptechLibraryAccessors(owner);
    private final FacebookLibraryAccessors laccForFacebookLibraryAccessors = new FacebookLibraryAccessors(owner);
    private final FirebaseLibraryAccessors laccForFirebaseLibraryAccessors = new FirebaseLibraryAccessors(owner);
    private final GoogleLibraryAccessors laccForGoogleLibraryAccessors = new GoogleLibraryAccessors(owner);
    private final InmobiLibraryAccessors laccForInmobiLibraryAccessors = new InmobiLibraryAccessors(owner);
    private final IntuitLibraryAccessors laccForIntuitLibraryAccessors = new IntuitLibraryAccessors(owner);
    private final IronsourceLibraryAccessors laccForIronsourceLibraryAccessors = new IronsourceLibraryAccessors(owner);
    private final LifecycleLibraryAccessors laccForLifecycleLibraryAccessors = new LifecycleLibraryAccessors(owner);
    private final MintegralLibraryAccessors laccForMintegralLibraryAccessors = new MintegralLibraryAccessors(owner);
    private final MolocoLibraryAccessors laccForMolocoLibraryAccessors = new MolocoLibraryAccessors(owner);
    private final OkhttpLibraryAccessors laccForOkhttpLibraryAccessors = new OkhttpLibraryAccessors(owner);
    private final PangleLibraryAccessors laccForPangleLibraryAccessors = new PangleLibraryAccessors(owner);
    private final PlayLibraryAccessors laccForPlayLibraryAccessors = new PlayLibraryAccessors(owner);
    private final PurchasesLibraryAccessors laccForPurchasesLibraryAccessors = new PurchasesLibraryAccessors(owner);
    private final RetrofitLibraryAccessors laccForRetrofitLibraryAccessors = new RetrofitLibraryAccessors(owner);
    private final RoomLibraryAccessors laccForRoomLibraryAccessors = new RoomLibraryAccessors(owner);
    private final SdkLibraryAccessors laccForSdkLibraryAccessors = new SdkLibraryAccessors(owner);
    private final SolarLibraryAccessors laccForSolarLibraryAccessors = new SolarLibraryAccessors(owner);
    private final UnityLibraryAccessors laccForUnityLibraryAccessors = new UnityLibraryAccessors(owner);
    private final UserLibraryAccessors laccForUserLibraryAccessors = new UserLibraryAccessors(owner);
    private final VungleLibraryAccessors laccForVungleLibraryAccessors = new VungleLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

    /**
     * Dependency provider for <b>cameraview</b> with <b>com.otaliastudios:cameraview</b> coordinates and
     * with version reference <b>cameraview</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getCameraview() {
        return create("cameraview");
    }

    /**
     * Dependency provider for <b>dexter</b> with <b>com.karumi:dexter</b> coordinates and
     * with version <b>6.2.3</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getDexter() {
        return create("dexter");
    }

    /**
     * Dependency provider for <b>dotsindicator</b> with <b>com.tbuonomo:dotsindicator</b> coordinates and
     * with version reference <b>dotsindicator</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getDotsindicator() {
        return create("dotsindicator");
    }

    /**
     * Dependency provider for <b>gson</b> with <b>com.google.code.gson:gson</b> coordinates and
     * with version reference <b>gson</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getGson() {
        return create("gson");
    }

    /**
     * Dependency provider for <b>junit</b> with <b>junit:junit</b> coordinates and
     * with version reference <b>junit</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getJunit() {
        return create("junit");
    }

    /**
     * Dependency provider for <b>localization</b> with <b>com.akexorcist:localization</b> coordinates and
     * with version <b>1.2.10</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getLocalization() {
        return create("localization");
    }

    /**
     * Dependency provider for <b>powermenu</b> with <b>com.github.skydoves:powermenu</b> coordinates and
     * with version <b>2.2.0</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getPowermenu() {
        return create("powermenu");
    }

    /**
     * Dependency provider for <b>ratingbar</b> with <b>com.github.yangmbin:RatingBar</b> coordinates and
     * with version <b>1.0.0</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getRatingbar() {
        return create("ratingbar");
    }

    /**
     * Dependency provider for <b>rxandroid</b> with <b>io.reactivex.rxjava3:rxandroid</b> coordinates and
     * with version <b>3.0.0</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getRxandroid() {
        return create("rxandroid");
    }

    /**
     * Dependency provider for <b>rxjava</b> with <b>io.reactivex.rxjava3:rxjava</b> coordinates and
     * with version <b>3.0.12</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getRxjava() {
        return create("rxjava");
    }

    /**
     * Dependency provider for <b>shimmer</b> with <b>com.facebook.shimmer:shimmer</b> coordinates and
     * with version reference <b>shimmer</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getShimmer() {
        return create("shimmer");
    }

    /**
     * Dependency provider for <b>spinkit</b> with <b>com.github.ybq:Android-SpinKit</b> coordinates and
     * with version <b>1.4.0</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getSpinkit() {
        return create("spinkit");
    }

    /**
     * Group of libraries at <b>airbnb</b>
     */
    public AirbnbLibraryAccessors getAirbnb() {
        return laccForAirbnbLibraryAccessors;
    }

    /**
     * Group of libraries at <b>androidx</b>
     */
    public AndroidxLibraryAccessors getAndroidx() {
        return laccForAndroidxLibraryAccessors;
    }

    /**
     * Group of libraries at <b>applovin</b>
     */
    public ApplovinLibraryAccessors getApplovin() {
        return laccForApplovinLibraryAccessors;
    }

    /**
     * Group of libraries at <b>audience</b>
     */
    public AudienceLibraryAccessors getAudience() {
        return laccForAudienceLibraryAccessors;
    }

    /**
     * Group of libraries at <b>bumptech</b>
     */
    public BumptechLibraryAccessors getBumptech() {
        return laccForBumptechLibraryAccessors;
    }

    /**
     * Group of libraries at <b>facebook</b>
     */
    public FacebookLibraryAccessors getFacebook() {
        return laccForFacebookLibraryAccessors;
    }

    /**
     * Group of libraries at <b>firebase</b>
     */
    public FirebaseLibraryAccessors getFirebase() {
        return laccForFirebaseLibraryAccessors;
    }

    /**
     * Group of libraries at <b>google</b>
     */
    public GoogleLibraryAccessors getGoogle() {
        return laccForGoogleLibraryAccessors;
    }

    /**
     * Group of libraries at <b>inmobi</b>
     */
    public InmobiLibraryAccessors getInmobi() {
        return laccForInmobiLibraryAccessors;
    }

    /**
     * Group of libraries at <b>intuit</b>
     */
    public IntuitLibraryAccessors getIntuit() {
        return laccForIntuitLibraryAccessors;
    }

    /**
     * Group of libraries at <b>ironsource</b>
     */
    public IronsourceLibraryAccessors getIronsource() {
        return laccForIronsourceLibraryAccessors;
    }

    /**
     * Group of libraries at <b>lifecycle</b>
     */
    public LifecycleLibraryAccessors getLifecycle() {
        return laccForLifecycleLibraryAccessors;
    }

    /**
     * Group of libraries at <b>mintegral</b>
     */
    public MintegralLibraryAccessors getMintegral() {
        return laccForMintegralLibraryAccessors;
    }

    /**
     * Group of libraries at <b>moloco</b>
     */
    public MolocoLibraryAccessors getMoloco() {
        return laccForMolocoLibraryAccessors;
    }

    /**
     * Group of libraries at <b>okhttp</b>
     */
    public OkhttpLibraryAccessors getOkhttp() {
        return laccForOkhttpLibraryAccessors;
    }

    /**
     * Group of libraries at <b>pangle</b>
     */
    public PangleLibraryAccessors getPangle() {
        return laccForPangleLibraryAccessors;
    }

    /**
     * Group of libraries at <b>play</b>
     */
    public PlayLibraryAccessors getPlay() {
        return laccForPlayLibraryAccessors;
    }

    /**
     * Group of libraries at <b>purchases</b>
     */
    public PurchasesLibraryAccessors getPurchases() {
        return laccForPurchasesLibraryAccessors;
    }

    /**
     * Group of libraries at <b>retrofit</b>
     */
    public RetrofitLibraryAccessors getRetrofit() {
        return laccForRetrofitLibraryAccessors;
    }

    /**
     * Group of libraries at <b>room</b>
     */
    public RoomLibraryAccessors getRoom() {
        return laccForRoomLibraryAccessors;
    }

    /**
     * Group of libraries at <b>sdk</b>
     */
    public SdkLibraryAccessors getSdk() {
        return laccForSdkLibraryAccessors;
    }

    /**
     * Group of libraries at <b>solar</b>
     */
    public SolarLibraryAccessors getSolar() {
        return laccForSolarLibraryAccessors;
    }

    /**
     * Group of libraries at <b>unity</b>
     */
    public UnityLibraryAccessors getUnity() {
        return laccForUnityLibraryAccessors;
    }

    /**
     * Group of libraries at <b>user</b>
     */
    public UserLibraryAccessors getUser() {
        return laccForUserLibraryAccessors;
    }

    /**
     * Group of libraries at <b>vungle</b>
     */
    public VungleLibraryAccessors getVungle() {
        return laccForVungleLibraryAccessors;
    }

    /**
     * Group of versions at <b>versions</b>
     */
    public VersionAccessors getVersions() {
        return vaccForVersionAccessors;
    }

    /**
     * Group of bundles at <b>bundles</b>
     */
    public BundleAccessors getBundles() {
        return baccForBundleAccessors;
    }

    /**
     * Group of plugins at <b>plugins</b>
     */
    public PluginAccessors getPlugins() {
        return paccForPluginAccessors;
    }

    public static class AirbnbLibraryAccessors extends SubDependencyFactory {

        public AirbnbLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>lottie</b> with <b>com.airbnb.android:lottie</b> coordinates and
         * with version reference <b>airbnbLottie</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getLottie() {
            return create("airbnb.lottie");
        }

    }

    public static class AndroidxLibraryAccessors extends SubDependencyFactory {
        private final AndroidxActivityLibraryAccessors laccForAndroidxActivityLibraryAccessors = new AndroidxActivityLibraryAccessors(owner);
        private final AndroidxComposeLibraryAccessors laccForAndroidxComposeLibraryAccessors = new AndroidxComposeLibraryAccessors(owner);
        private final AndroidxCoreLibraryAccessors laccForAndroidxCoreLibraryAccessors = new AndroidxCoreLibraryAccessors(owner);
        private final AndroidxEspressoLibraryAccessors laccForAndroidxEspressoLibraryAccessors = new AndroidxEspressoLibraryAccessors(owner);
        private final AndroidxJunitLibraryAccessors laccForAndroidxJunitLibraryAccessors = new AndroidxJunitLibraryAccessors(owner);
        private final AndroidxLifecycleLibraryAccessors laccForAndroidxLifecycleLibraryAccessors = new AndroidxLifecycleLibraryAccessors(owner);
        private final AndroidxNavigationLibraryAccessors laccForAndroidxNavigationLibraryAccessors = new AndroidxNavigationLibraryAccessors(owner);
        private final AndroidxUiLibraryAccessors laccForAndroidxUiLibraryAccessors = new AndroidxUiLibraryAccessors(owner);

        public AndroidxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>annotation</b> with <b>androidx.annotation:annotation</b> coordinates and
         * with version <b>1.0.0</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAnnotation() {
            return create("androidx.annotation");
        }

        /**
         * Dependency provider for <b>appcompat</b> with <b>androidx.appcompat:appcompat</b> coordinates and
         * with version reference <b>appcompat</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAppcompat() {
            return create("androidx.appcompat");
        }

        /**
         * Dependency provider for <b>constraintlayout</b> with <b>androidx.constraintlayout:constraintlayout</b> coordinates and
         * with version reference <b>constraintlayout</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getConstraintlayout() {
            return create("androidx.constraintlayout");
        }

        /**
         * Dependency provider for <b>material3</b> with <b>androidx.compose.material3:material3</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMaterial3() {
            return create("androidx.material3");
        }

        /**
         * Dependency provider for <b>multidex</b> with <b>androidx.multidex:multidex</b> coordinates and
         * with version <b>2.0.1</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMultidex() {
            return create("androidx.multidex");
        }

        /**
         * Dependency provider for <b>work</b> with <b>androidx.work:work-runtime-ktx</b> coordinates and
         * with version reference <b>work</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getWork() {
            return create("androidx.work");
        }

        /**
         * Group of libraries at <b>androidx.activity</b>
         */
        public AndroidxActivityLibraryAccessors getActivity() {
            return laccForAndroidxActivityLibraryAccessors;
        }

        /**
         * Group of libraries at <b>androidx.compose</b>
         */
        public AndroidxComposeLibraryAccessors getCompose() {
            return laccForAndroidxComposeLibraryAccessors;
        }

        /**
         * Group of libraries at <b>androidx.core</b>
         */
        public AndroidxCoreLibraryAccessors getCore() {
            return laccForAndroidxCoreLibraryAccessors;
        }

        /**
         * Group of libraries at <b>androidx.espresso</b>
         */
        public AndroidxEspressoLibraryAccessors getEspresso() {
            return laccForAndroidxEspressoLibraryAccessors;
        }

        /**
         * Group of libraries at <b>androidx.junit</b>
         */
        public AndroidxJunitLibraryAccessors getJunit() {
            return laccForAndroidxJunitLibraryAccessors;
        }

        /**
         * Group of libraries at <b>androidx.lifecycle</b>
         */
        public AndroidxLifecycleLibraryAccessors getLifecycle() {
            return laccForAndroidxLifecycleLibraryAccessors;
        }

        /**
         * Group of libraries at <b>androidx.navigation</b>
         */
        public AndroidxNavigationLibraryAccessors getNavigation() {
            return laccForAndroidxNavigationLibraryAccessors;
        }

        /**
         * Group of libraries at <b>androidx.ui</b>
         */
        public AndroidxUiLibraryAccessors getUi() {
            return laccForAndroidxUiLibraryAccessors;
        }

    }

    public static class AndroidxActivityLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public AndroidxActivityLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>activity</b> with <b>androidx.activity:activity</b> coordinates and
         * with version reference <b>activity</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> asProvider() {
            return create("androidx.activity");
        }

        /**
         * Dependency provider for <b>compose</b> with <b>androidx.activity:activity-compose</b> coordinates and
         * with version reference <b>activityCompose</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCompose() {
            return create("androidx.activity.compose");
        }

    }

    public static class AndroidxComposeLibraryAccessors extends SubDependencyFactory {

        public AndroidxComposeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>bom</b> with <b>androidx.compose:compose-bom</b> coordinates and
         * with version reference <b>composeBom</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getBom() {
            return create("androidx.compose.bom");
        }

    }

    public static class AndroidxCoreLibraryAccessors extends SubDependencyFactory {

        public AndroidxCoreLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>ktx</b> with <b>androidx.core:core-ktx</b> coordinates and
         * with version reference <b>coreKtx</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getKtx() {
            return create("androidx.core.ktx");
        }

    }

    public static class AndroidxEspressoLibraryAccessors extends SubDependencyFactory {

        public AndroidxEspressoLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>core</b> with <b>androidx.test.espresso:espresso-core</b> coordinates and
         * with version reference <b>espressoCore</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("androidx.espresso.core");
        }

    }

    public static class AndroidxJunitLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public AndroidxJunitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>junit</b> with <b>androidx.test.ext:junit</b> coordinates and
         * with version reference <b>junitVersion</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> asProvider() {
            return create("androidx.junit");
        }

        /**
         * Dependency provider for <b>ktx</b> with <b>androidx.test.ext:junit-ktx</b> coordinates and
         * with version reference <b>junitver</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getKtx() {
            return create("androidx.junit.ktx");
        }

    }

    public static class AndroidxLifecycleLibraryAccessors extends SubDependencyFactory {
        private final AndroidxLifecycleLivedataLibraryAccessors laccForAndroidxLifecycleLivedataLibraryAccessors = new AndroidxLifecycleLivedataLibraryAccessors(owner);
        private final AndroidxLifecycleRuntimeLibraryAccessors laccForAndroidxLifecycleRuntimeLibraryAccessors = new AndroidxLifecycleRuntimeLibraryAccessors(owner);

        public AndroidxLifecycleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>androidx.lifecycle.livedata</b>
         */
        public AndroidxLifecycleLivedataLibraryAccessors getLivedata() {
            return laccForAndroidxLifecycleLivedataLibraryAccessors;
        }

        /**
         * Group of libraries at <b>androidx.lifecycle.runtime</b>
         */
        public AndroidxLifecycleRuntimeLibraryAccessors getRuntime() {
            return laccForAndroidxLifecycleRuntimeLibraryAccessors;
        }

    }

    public static class AndroidxLifecycleLivedataLibraryAccessors extends SubDependencyFactory {

        public AndroidxLifecycleLivedataLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>ktx</b> with <b>androidx.lifecycle:lifecycle-livedata-ktx</b> coordinates and
         * with version reference <b>room</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getKtx() {
            return create("androidx.lifecycle.livedata.ktx");
        }

    }

    public static class AndroidxLifecycleRuntimeLibraryAccessors extends SubDependencyFactory {

        public AndroidxLifecycleRuntimeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>ktx</b> with <b>androidx.lifecycle:lifecycle-runtime-ktx</b> coordinates and
         * with version reference <b>lifecycleRuntimeKtx</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getKtx() {
            return create("androidx.lifecycle.runtime.ktx");
        }

    }

    public static class AndroidxNavigationLibraryAccessors extends SubDependencyFactory {

        public AndroidxNavigationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>fragment</b> with <b>androidx.navigation:navigation-fragment</b> coordinates and
         * with version reference <b>navigationFragment</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getFragment() {
            return create("androidx.navigation.fragment");
        }

        /**
         * Dependency provider for <b>ui</b> with <b>androidx.navigation:navigation-ui</b> coordinates and
         * with version reference <b>navigationUi</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getUi() {
            return create("androidx.navigation.ui");
        }

    }

    public static class AndroidxUiLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {
        private final AndroidxUiTestLibraryAccessors laccForAndroidxUiTestLibraryAccessors = new AndroidxUiTestLibraryAccessors(owner);
        private final AndroidxUiToolingLibraryAccessors laccForAndroidxUiToolingLibraryAccessors = new AndroidxUiToolingLibraryAccessors(owner);

        public AndroidxUiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>ui</b> with <b>androidx.compose.ui:ui</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> asProvider() {
            return create("androidx.ui");
        }

        /**
         * Dependency provider for <b>graphics</b> with <b>androidx.compose.ui:ui-graphics</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getGraphics() {
            return create("androidx.ui.graphics");
        }

        /**
         * Group of libraries at <b>androidx.ui.test</b>
         */
        public AndroidxUiTestLibraryAccessors getTest() {
            return laccForAndroidxUiTestLibraryAccessors;
        }

        /**
         * Group of libraries at <b>androidx.ui.tooling</b>
         */
        public AndroidxUiToolingLibraryAccessors getTooling() {
            return laccForAndroidxUiToolingLibraryAccessors;
        }

    }

    public static class AndroidxUiTestLibraryAccessors extends SubDependencyFactory {

        public AndroidxUiTestLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>junit4</b> with <b>androidx.compose.ui:ui-test-junit4</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJunit4() {
            return create("androidx.ui.test.junit4");
        }

        /**
         * Dependency provider for <b>manifest</b> with <b>androidx.compose.ui:ui-test-manifest</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getManifest() {
            return create("androidx.ui.test.manifest");
        }

    }

    public static class AndroidxUiToolingLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public AndroidxUiToolingLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>tooling</b> with <b>androidx.compose.ui:ui-tooling</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> asProvider() {
            return create("androidx.ui.tooling");
        }

        /**
         * Dependency provider for <b>preview</b> with <b>androidx.compose.ui:ui-tooling-preview</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getPreview() {
            return create("androidx.ui.tooling.preview");
        }

    }

    public static class ApplovinLibraryAccessors extends SubDependencyFactory {
        private final ApplovinBytedanceLibraryAccessors laccForApplovinBytedanceLibraryAccessors = new ApplovinBytedanceLibraryAccessors(owner);
        private final ApplovinFacebookLibraryAccessors laccForApplovinFacebookLibraryAccessors = new ApplovinFacebookLibraryAccessors(owner);
        private final ApplovinGoogleLibraryAccessors laccForApplovinGoogleLibraryAccessors = new ApplovinGoogleLibraryAccessors(owner);
        private final ApplovinMintegralLibraryAccessors laccForApplovinMintegralLibraryAccessors = new ApplovinMintegralLibraryAccessors(owner);
        private final ApplovinUnityLibraryAccessors laccForApplovinUnityLibraryAccessors = new ApplovinUnityLibraryAccessors(owner);

        public ApplovinLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>mediation</b> with <b>com.google.ads.mediation:applovin</b> coordinates and
         * with version reference <b>applovin</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMediation() {
            return create("applovin.mediation");
        }

        /**
         * Dependency provider for <b>sdk</b> with <b>com.applovin:applovin-sdk</b> coordinates and
         * with version reference <b>applovinSdk</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getSdk() {
            return create("applovin.sdk");
        }

        /**
         * Group of libraries at <b>applovin.bytedance</b>
         */
        public ApplovinBytedanceLibraryAccessors getBytedance() {
            return laccForApplovinBytedanceLibraryAccessors;
        }

        /**
         * Group of libraries at <b>applovin.facebook</b>
         */
        public ApplovinFacebookLibraryAccessors getFacebook() {
            return laccForApplovinFacebookLibraryAccessors;
        }

        /**
         * Group of libraries at <b>applovin.google</b>
         */
        public ApplovinGoogleLibraryAccessors getGoogle() {
            return laccForApplovinGoogleLibraryAccessors;
        }

        /**
         * Group of libraries at <b>applovin.mintegral</b>
         */
        public ApplovinMintegralLibraryAccessors getMintegral() {
            return laccForApplovinMintegralLibraryAccessors;
        }

        /**
         * Group of libraries at <b>applovin.unity</b>
         */
        public ApplovinUnityLibraryAccessors getUnity() {
            return laccForApplovinUnityLibraryAccessors;
        }

    }

    public static class ApplovinBytedanceLibraryAccessors extends SubDependencyFactory {

        public ApplovinBytedanceLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>adapter</b> with <b>com.applovin.mediation:bytedance-adapter</b> coordinates and
         * with version reference <b>applovinBytedanceAdapter</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAdapter() {
            return create("applovin.bytedance.adapter");
        }

    }

    public static class ApplovinFacebookLibraryAccessors extends SubDependencyFactory {

        public ApplovinFacebookLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>adapter</b> with <b>com.applovin.mediation:facebook-adapter</b> coordinates and
         * with version reference <b>applovinFacebookAdapter</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAdapter() {
            return create("applovin.facebook.adapter");
        }

    }

    public static class ApplovinGoogleLibraryAccessors extends SubDependencyFactory {

        public ApplovinGoogleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>adapter</b> with <b>com.applovin.mediation:google-adapter</b> coordinates and
         * with version reference <b>applovinGoogleAdapter</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAdapter() {
            return create("applovin.google.adapter");
        }

    }

    public static class ApplovinMintegralLibraryAccessors extends SubDependencyFactory {

        public ApplovinMintegralLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>adapter</b> with <b>com.applovin.mediation:mintegral-adapter</b> coordinates and
         * with version reference <b>applovinMintegralAdapter</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAdapter() {
            return create("applovin.mintegral.adapter");
        }

    }

    public static class ApplovinUnityLibraryAccessors extends SubDependencyFactory {

        public ApplovinUnityLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>adapter</b> with <b>com.applovin.mediation:unityads-adapter</b> coordinates and
         * with version reference <b>applovinUnityAdapter</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAdapter() {
            return create("applovin.unity.adapter");
        }

    }

    public static class AudienceLibraryAccessors extends SubDependencyFactory {
        private final AudienceNetworkLibraryAccessors laccForAudienceNetworkLibraryAccessors = new AudienceNetworkLibraryAccessors(owner);

        public AudienceLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>audience.network</b>
         */
        public AudienceNetworkLibraryAccessors getNetwork() {
            return laccForAudienceNetworkLibraryAccessors;
        }

    }

    public static class AudienceNetworkLibraryAccessors extends SubDependencyFactory {

        public AudienceNetworkLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>sdk</b> with <b>com.facebook.android:audience-network-sdk</b> coordinates and
         * with version <b>6.17.0</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getSdk() {
            return create("audience.network.sdk");
        }

    }

    public static class BumptechLibraryAccessors extends SubDependencyFactory {

        public BumptechLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>glide</b> with <b>com.github.bumptech.glide:glide</b> coordinates and
         * with version reference <b>glide</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getGlide() {
            return create("bumptech.glide");
        }

    }

    public static class FacebookLibraryAccessors extends SubDependencyFactory {

        public FacebookLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>mediation</b> with <b>com.google.ads.mediation:facebook</b> coordinates and
         * with version reference <b>facebook</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMediation() {
            return create("facebook.mediation");
        }

        /**
         * Dependency provider for <b>sdk</b> with <b>com.facebook.android:facebook-android-sdk</b> coordinates and
         * with version reference <b>facebookSdk</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getSdk() {
            return create("facebook.sdk");
        }

    }

    public static class FirebaseLibraryAccessors extends SubDependencyFactory {
        private final FirebaseAnalyticsLibraryAccessors laccForFirebaseAnalyticsLibraryAccessors = new FirebaseAnalyticsLibraryAccessors(owner);
        private final FirebaseCrashlyticsLibraryAccessors laccForFirebaseCrashlyticsLibraryAccessors = new FirebaseCrashlyticsLibraryAccessors(owner);

        public FirebaseLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>bom</b> with <b>com.google.firebase:firebase-bom</b> coordinates and
         * with version reference <b>firebaseBom</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getBom() {
            return create("firebase.bom");
        }

        /**
         * Dependency provider for <b>config</b> with <b>com.google.firebase:firebase-config</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getConfig() {
            return create("firebase.config");
        }

        /**
         * Dependency provider for <b>database</b> with <b>com.google.firebase:firebase-database</b> coordinates and
         * with version <b>20.2.2</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getDatabase() {
            return create("firebase.database");
        }

        /**
         * Dependency provider for <b>messaging</b> with <b>com.google.firebase:firebase-messaging</b> coordinates and
         * with version reference <b>firebaseMessaging</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMessaging() {
            return create("firebase.messaging");
        }

        /**
         * Group of libraries at <b>firebase.analytics</b>
         */
        public FirebaseAnalyticsLibraryAccessors getAnalytics() {
            return laccForFirebaseAnalyticsLibraryAccessors;
        }

        /**
         * Group of libraries at <b>firebase.crashlytics</b>
         */
        public FirebaseCrashlyticsLibraryAccessors getCrashlytics() {
            return laccForFirebaseCrashlyticsLibraryAccessors;
        }

    }

    public static class FirebaseAnalyticsLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public FirebaseAnalyticsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>analytics</b> with <b>com.google.firebase:firebase-analytics</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> asProvider() {
            return create("firebase.analytics");
        }

        /**
         * Dependency provider for <b>ktx</b> with <b>com.google.firebase:firebase-analytics-ktx</b> coordinates and
         * with version <b>21.3.0</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getKtx() {
            return create("firebase.analytics.ktx");
        }

    }

    public static class FirebaseCrashlyticsLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public FirebaseCrashlyticsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>crashlytics</b> with <b>com.google.firebase:firebase-crashlytics</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> asProvider() {
            return create("firebase.crashlytics");
        }

        /**
         * Dependency provider for <b>ktx</b> with <b>com.google.firebase:firebase-crashlytics-ktx</b> coordinates and
         * with version <b>18.4.1</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getKtx() {
            return create("firebase.crashlytics.ktx");
        }

    }

    public static class GoogleLibraryAccessors extends SubDependencyFactory {
        private final GooglePlayLibraryAccessors laccForGooglePlayLibraryAccessors = new GooglePlayLibraryAccessors(owner);

        public GoogleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>hilt</b> with <b>com.google.dagger:hilt-android</b> coordinates and
         * with version reference <b>hiltVersion</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getHilt() {
            return create("google.hilt");
        }

        /**
         * Dependency provider for <b>material</b> with <b>com.google.android.material:material</b> coordinates and
         * with version reference <b>googleMaterial</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMaterial() {
            return create("google.material");
        }

        /**
         * Group of libraries at <b>google.play</b>
         */
        public GooglePlayLibraryAccessors getPlay() {
            return laccForGooglePlayLibraryAccessors;
        }

    }

    public static class GooglePlayLibraryAccessors extends SubDependencyFactory {
        private final GooglePlayCoreLibraryAccessors laccForGooglePlayCoreLibraryAccessors = new GooglePlayCoreLibraryAccessors(owner);

        public GooglePlayLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>google.play.core</b>
         */
        public GooglePlayCoreLibraryAccessors getCore() {
            return laccForGooglePlayCoreLibraryAccessors;
        }

    }

    public static class GooglePlayCoreLibraryAccessors extends SubDependencyFactory {

        public GooglePlayCoreLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>review</b> with <b>com.google.android.play:review</b> coordinates and
         * with version reference <b>googlePlayReview</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getReview() {
            return create("google.play.core.review");
        }

    }

    public static class InmobiLibraryAccessors extends SubDependencyFactory {

        public InmobiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>mediation</b> with <b>com.google.ads.mediation:inmobi</b> coordinates and
         * with version reference <b>inmobiMediation</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMediation() {
            return create("inmobi.mediation");
        }

    }

    public static class IntuitLibraryAccessors extends SubDependencyFactory {

        public IntuitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>sdp</b> with <b>com.intuit.sdp:sdp-android</b> coordinates and
         * with version reference <b>intuitSdp</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getSdp() {
            return create("intuit.sdp");
        }

    }

    public static class IronsourceLibraryAccessors extends SubDependencyFactory {

        public IronsourceLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>mediation</b> with <b>com.google.ads.mediation:ironsource</b> coordinates and
         * with version reference <b>ironsourceMediation</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMediation() {
            return create("ironsource.mediation");
        }

    }

    public static class LifecycleLibraryAccessors extends SubDependencyFactory {

        public LifecycleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>compiler</b> with <b>android.arch.lifecycle:compiler</b> coordinates and
         * with version <b>1.1.1</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCompiler() {
            return create("lifecycle.compiler");
        }

        /**
         * Dependency provider for <b>extensions</b> with <b>android.arch.lifecycle:extensions</b> coordinates and
         * with version <b>1.1.1</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getExtensions() {
            return create("lifecycle.extensions");
        }

    }

    public static class MintegralLibraryAccessors extends SubDependencyFactory {

        public MintegralLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>mediation</b> with <b>com.google.ads.mediation:mintegral</b> coordinates and
         * with version reference <b>mintegral</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMediation() {
            return create("mintegral.mediation");
        }

    }

    public static class MolocoLibraryAccessors extends SubDependencyFactory {

        public MolocoLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>mediation</b> with <b>com.google.ads.mediation:moloco</b> coordinates and
         * with version reference <b>molocoMediation</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMediation() {
            return create("moloco.mediation");
        }

    }

    public static class OkhttpLibraryAccessors extends SubDependencyFactory {
        private final OkhttpLoggingLibraryAccessors laccForOkhttpLoggingLibraryAccessors = new OkhttpLoggingLibraryAccessors(owner);

        public OkhttpLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>okhttp.logging</b>
         */
        public OkhttpLoggingLibraryAccessors getLogging() {
            return laccForOkhttpLoggingLibraryAccessors;
        }

    }

    public static class OkhttpLoggingLibraryAccessors extends SubDependencyFactory {

        public OkhttpLoggingLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>interceptor</b> with <b>com.squareup.okhttp3:logging-interceptor</b> coordinates and
         * with version reference <b>okhttp</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getInterceptor() {
            return create("okhttp.logging.interceptor");
        }

    }

    public static class PangleLibraryAccessors extends SubDependencyFactory {

        public PangleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>mediation</b> with <b>com.google.ads.mediation:pangle</b> coordinates and
         * with version reference <b>pangle</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMediation() {
            return create("pangle.mediation");
        }

    }

    public static class PlayLibraryAccessors extends SubDependencyFactory {
        private final PlayReviewLibraryAccessors laccForPlayReviewLibraryAccessors = new PlayReviewLibraryAccessors(owner);
        private final PlayServicesLibraryAccessors laccForPlayServicesLibraryAccessors = new PlayServicesLibraryAccessors(owner);

        public PlayLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>play.review</b>
         */
        public PlayReviewLibraryAccessors getReview() {
            return laccForPlayReviewLibraryAccessors;
        }

        /**
         * Group of libraries at <b>play.services</b>
         */
        public PlayServicesLibraryAccessors getServices() {
            return laccForPlayServicesLibraryAccessors;
        }

    }

    public static class PlayReviewLibraryAccessors extends SubDependencyFactory {

        public PlayReviewLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>ktx</b> with <b>com.google.android.play:review-ktx</b> coordinates and
         * with version <b>2.0.1</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getKtx() {
            return create("play.review.ktx");
        }

    }

    public static class PlayServicesLibraryAccessors extends SubDependencyFactory {

        public PlayServicesLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>ads</b> with <b>com.google.android.gms:play-services-ads</b> coordinates and
         * with version reference <b>playServicesAds</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAds() {
            return create("play.services.ads");
        }

        /**
         * Dependency provider for <b>vision</b> with <b>com.google.android.gms:play-services-vision</b> coordinates and
         * with version reference <b>playServicesVision</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getVision() {
            return create("play.services.vision");
        }

    }

    public static class PurchasesLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public PurchasesLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>purchases</b> with <b>com.revenuecat.purchases:purchases</b> coordinates and
         * with version reference <b>purchases</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> asProvider() {
            return create("purchases");
        }

        /**
         * Dependency provider for <b>v700</b> with <b>com.revenuecat.purchases:purchases</b> coordinates and
         * with version reference <b>purchasesVersion</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getV700() {
            return create("purchases.v700");
        }

    }

    public static class RetrofitLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {
        private final RetrofitConverterLibraryAccessors laccForRetrofitConverterLibraryAccessors = new RetrofitConverterLibraryAccessors(owner);

        public RetrofitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>retrofit</b> with <b>com.squareup.retrofit2:retrofit</b> coordinates and
         * with version reference <b>retrofit</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> asProvider() {
            return create("retrofit");
        }

        /**
         * Group of libraries at <b>retrofit.converter</b>
         */
        public RetrofitConverterLibraryAccessors getConverter() {
            return laccForRetrofitConverterLibraryAccessors;
        }

    }

    public static class RetrofitConverterLibraryAccessors extends SubDependencyFactory {

        public RetrofitConverterLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>gson</b> with <b>com.squareup.retrofit2:converter-gson</b> coordinates and
         * with version reference <b>retrofit</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getGson() {
            return create("retrofit.converter.gson");
        }

        /**
         * Dependency provider for <b>scalars</b> with <b>com.squareup.retrofit2:converter-scalars</b> coordinates and
         * with version reference <b>retrofit</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getScalars() {
            return create("retrofit.converter.scalars");
        }

    }

    public static class RoomLibraryAccessors extends SubDependencyFactory {

        public RoomLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>compiler</b> with <b>androidx.room:room-compiler</b> coordinates and
         * with version reference <b>room</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCompiler() {
            return create("room.compiler");
        }

        /**
         * Dependency provider for <b>ktx</b> with <b>androidx.room:room-ktx</b> coordinates and
         * with version reference <b>room</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getKtx() {
            return create("room.ktx");
        }

        /**
         * Dependency provider for <b>runtime</b> with <b>androidx.room:room-runtime</b> coordinates and
         * with version reference <b>room</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getRuntime() {
            return create("room.runtime");
        }

    }

    public static class SdkLibraryAccessors extends SubDependencyFactory {

        public SdkLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>android</b> with <b>io.airbridge:sdk-android</b> coordinates and
         * with version reference <b>sdkAndroid</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAndroid() {
            return create("sdk.android");
        }

    }

    public static class SolarLibraryAccessors extends SubDependencyFactory {

        public SolarLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>engine</b> with <b>com.reyun.solar.engine.oversea:solar-engine-core</b> coordinates and
         * with version <b>1.2.9.2</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getEngine() {
            return create("solar.engine");
        }

    }

    public static class UnityLibraryAccessors extends SubDependencyFactory {

        public UnityLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>ads</b> with <b>com.unity3d.ads:unity-ads</b> coordinates and
         * with version reference <b>unityAds</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAds() {
            return create("unity.ads");
        }

        /**
         * Dependency provider for <b>mediation</b> with <b>com.google.ads.mediation:unity</b> coordinates and
         * with version reference <b>unityMediation</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMediation() {
            return create("unity.mediation");
        }

    }

    public static class UserLibraryAccessors extends SubDependencyFactory {
        private final UserMessagingLibraryAccessors laccForUserMessagingLibraryAccessors = new UserMessagingLibraryAccessors(owner);

        public UserLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>user.messaging</b>
         */
        public UserMessagingLibraryAccessors getMessaging() {
            return laccForUserMessagingLibraryAccessors;
        }

    }

    public static class UserMessagingLibraryAccessors extends SubDependencyFactory {

        public UserMessagingLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>platform</b> with <b>com.google.android.ump:user-messaging-platform</b> coordinates and
         * with version reference <b>userMessagingPlatform</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getPlatform() {
            return create("user.messaging.platform");
        }

    }

    public static class VungleLibraryAccessors extends SubDependencyFactory {

        public VungleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>mediation</b> with <b>com.google.ads.mediation:vungle</b> coordinates and
         * with version reference <b>vungleMediation</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMediation() {
            return create("vungle.mediation");
        }

    }

    public static class VersionAccessors extends VersionFactory  {

        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>activity</b> with value <b>1.9.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getActivity() { return getVersion("activity"); }

        /**
         * Version alias <b>activityCompose</b> with value <b>1.9.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getActivityCompose() { return getVersion("activityCompose"); }

        /**
         * Version alias <b>agp</b> with value <b>8.4.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getAgp() { return getVersion("agp"); }

        /**
         * Version alias <b>airbnbLottie</b> with value <b>6.4.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getAirbnbLottie() { return getVersion("airbnbLottie"); }

        /**
         * Version alias <b>appcompat</b> with value <b>1.7.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getAppcompat() { return getVersion("appcompat"); }

        /**
         * Version alias <b>applovin</b> with value <b>13.4.0.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getApplovin() { return getVersion("applovin"); }

        /**
         * Version alias <b>applovinBytedanceAdapter</b> with value <b>7.6.0.3.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getApplovinBytedanceAdapter() { return getVersion("applovinBytedanceAdapter"); }

        /**
         * Version alias <b>applovinFacebookAdapter</b> with value <b>6.20.0.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getApplovinFacebookAdapter() { return getVersion("applovinFacebookAdapter"); }

        /**
         * Version alias <b>applovinGoogleAdapter</b> with value <b>24.5.0.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getApplovinGoogleAdapter() { return getVersion("applovinGoogleAdapter"); }

        /**
         * Version alias <b>applovinMintegralAdapter</b> with value <b>16.9.91.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getApplovinMintegralAdapter() { return getVersion("applovinMintegralAdapter"); }

        /**
         * Version alias <b>applovinSdk</b> with value <b>13.4.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getApplovinSdk() { return getVersion("applovinSdk"); }

        /**
         * Version alias <b>applovinUnityAdapter</b> with value <b>4.16.1.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getApplovinUnityAdapter() { return getVersion("applovinUnityAdapter"); }

        /**
         * Version alias <b>cameraview</b> with value <b>2.7.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCameraview() { return getVersion("cameraview"); }

        /**
         * Version alias <b>composeBom</b> with value <b>2023.08.00</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getComposeBom() { return getVersion("composeBom"); }

        /**
         * Version alias <b>constraintlayout</b> with value <b>2.1.4</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getConstraintlayout() { return getVersion("constraintlayout"); }

        /**
         * Version alias <b>coreKtx</b> with value <b>1.13.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCoreKtx() { return getVersion("coreKtx"); }

        /**
         * Version alias <b>crashlytics</b> with value <b>3.0.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCrashlytics() { return getVersion("crashlytics"); }

        /**
         * Version alias <b>dotsindicator</b> with value <b>5.1.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getDotsindicator() { return getVersion("dotsindicator"); }

        /**
         * Version alias <b>espressoCore</b> with value <b>3.5.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getEspressoCore() { return getVersion("espressoCore"); }

        /**
         * Version alias <b>facebook</b> with value <b>6.20.0.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getFacebook() { return getVersion("facebook"); }

        /**
         * Version alias <b>facebookSdk</b> with value <b>latest.release</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getFacebookSdk() { return getVersion("facebookSdk"); }

        /**
         * Version alias <b>firebaseAppdistribution</b> with value <b>5.0.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getFirebaseAppdistribution() { return getVersion("firebaseAppdistribution"); }

        /**
         * Version alias <b>firebaseBom</b> with value <b>33.3.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getFirebaseBom() { return getVersion("firebaseBom"); }

        /**
         * Version alias <b>firebaseMessaging</b> with value <b>24.0.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getFirebaseMessaging() { return getVersion("firebaseMessaging"); }

        /**
         * Version alias <b>glide</b> with value <b>5.0.0-rc01</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getGlide() { return getVersion("glide"); }

        /**
         * Version alias <b>googleMaterial</b> with value <b>1.12.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getGoogleMaterial() { return getVersion("googleMaterial"); }

        /**
         * Version alias <b>googlePlayReview</b> with value <b>2.0.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getGooglePlayReview() { return getVersion("googlePlayReview"); }

        /**
         * Version alias <b>googleServices</b> with value <b>4.4.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getGoogleServices() { return getVersion("googleServices"); }

        /**
         * Version alias <b>gson</b> with value <b>2.11.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getGson() { return getVersion("gson"); }

        /**
         * Version alias <b>hiltVersion</b> with value <b>2.47</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getHiltVersion() { return getVersion("hiltVersion"); }

        /**
         * Version alias <b>inmobiMediation</b> with value <b>10.8.8.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getInmobiMediation() { return getVersion("inmobiMediation"); }

        /**
         * Version alias <b>intuitSdp</b> with value <b>1.1.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getIntuitSdp() { return getVersion("intuitSdp"); }

        /**
         * Version alias <b>ironsourceMediation</b> with value <b>9.0.0.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getIronsourceMediation() { return getVersion("ironsourceMediation"); }

        /**
         * Version alias <b>junit</b> with value <b>4.13.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJunit() { return getVersion("junit"); }

        /**
         * Version alias <b>junitVersion</b> with value <b>1.1.5</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJunitVersion() { return getVersion("junitVersion"); }

        /**
         * Version alias <b>junitver</b> with value <b>1.2.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJunitver() { return getVersion("junitver"); }

        /**
         * Version alias <b>kotlin</b> with value <b>2.1.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getKotlin() { return getVersion("kotlin"); }

        /**
         * Version alias <b>ksp</b> with value <b>2.1.0-1.0.29</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getKsp() { return getVersion("ksp"); }

        /**
         * Version alias <b>lifecycleRuntimeKtx</b> with value <b>2.8.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getLifecycleRuntimeKtx() { return getVersion("lifecycleRuntimeKtx"); }

        /**
         * Version alias <b>mintegral</b> with value <b>16.9.91.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getMintegral() { return getVersion("mintegral"); }

        /**
         * Version alias <b>molocoMediation</b> with value <b>4.1.1.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getMolocoMediation() { return getVersion("molocoMediation"); }

        /**
         * Version alias <b>navigationFragment</b> with value <b>2.8.5</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getNavigationFragment() { return getVersion("navigationFragment"); }

        /**
         * Version alias <b>navigationUi</b> with value <b>2.8.5</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getNavigationUi() { return getVersion("navigationUi"); }

        /**
         * Version alias <b>okhttp</b> with value <b>4.12.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getOkhttp() { return getVersion("okhttp"); }

        /**
         * Version alias <b>pangle</b> with value <b>7.6.0.3.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getPangle() { return getVersion("pangle"); }

        /**
         * Version alias <b>playServicesAds</b> with value <b>24.5.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getPlayServicesAds() { return getVersion("playServicesAds"); }

        /**
         * Version alias <b>playServicesVision</b> with value <b>18.0.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getPlayServicesVision() { return getVersion("playServicesVision"); }

        /**
         * Version alias <b>purchases</b> with value <b>7.10.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getPurchases() { return getVersion("purchases"); }

        /**
         * Version alias <b>purchasesVersion</b> with value <b>7.0.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getPurchasesVersion() { return getVersion("purchasesVersion"); }

        /**
         * Version alias <b>retrofit</b> with value <b>2.9.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getRetrofit() { return getVersion("retrofit"); }

        /**
         * Version alias <b>room</b> with value <b>2.8.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getRoom() { return getVersion("room"); }

        /**
         * Version alias <b>sdkAndroid</b> with value <b>4.7.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getSdkAndroid() { return getVersion("sdkAndroid"); }

        /**
         * Version alias <b>shimmer</b> with value <b>0.5.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getShimmer() { return getVersion("shimmer"); }

        /**
         * Version alias <b>unityAds</b> with value <b>4.16.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getUnityAds() { return getVersion("unityAds"); }

        /**
         * Version alias <b>unityMediation</b> with value <b>4.16.1.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getUnityMediation() { return getVersion("unityMediation"); }

        /**
         * Version alias <b>userMessagingPlatform</b> with value <b>3.0.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getUserMessagingPlatform() { return getVersion("userMessagingPlatform"); }

        /**
         * Version alias <b>vungleMediation</b> with value <b>7.5.1.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getVungleMediation() { return getVersion("vungleMediation"); }

        /**
         * Version alias <b>work</b> with value <b>2.10.5</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getWork() { return getVersion("work"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

    }

    public static class PluginAccessors extends PluginFactory {
        private final AndroidPluginAccessors paccForAndroidPluginAccessors = new AndroidPluginAccessors(providers, config);
        private final ComposePluginAccessors paccForComposePluginAccessors = new ComposePluginAccessors(providers, config);
        private final FirebasePluginAccessors paccForFirebasePluginAccessors = new FirebasePluginAccessors(providers, config);
        private final GooglePluginAccessors paccForGooglePluginAccessors = new GooglePluginAccessors(providers, config);
        private final JetbrainsPluginAccessors paccForJetbrainsPluginAccessors = new JetbrainsPluginAccessors(providers, config);

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of plugins at <b>plugins.android</b>
         */
        public AndroidPluginAccessors getAndroid() {
            return paccForAndroidPluginAccessors;
        }

        /**
         * Group of plugins at <b>plugins.compose</b>
         */
        public ComposePluginAccessors getCompose() {
            return paccForComposePluginAccessors;
        }

        /**
         * Group of plugins at <b>plugins.firebase</b>
         */
        public FirebasePluginAccessors getFirebase() {
            return paccForFirebasePluginAccessors;
        }

        /**
         * Group of plugins at <b>plugins.google</b>
         */
        public GooglePluginAccessors getGoogle() {
            return paccForGooglePluginAccessors;
        }

        /**
         * Group of plugins at <b>plugins.jetbrains</b>
         */
        public JetbrainsPluginAccessors getJetbrains() {
            return paccForJetbrainsPluginAccessors;
        }

    }

    public static class AndroidPluginAccessors extends PluginFactory {

        public AndroidPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Plugin provider for <b>android.application</b> with plugin id <b>com.android.application</b> and
         * with version reference <b>agp</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getApplication() { return createPlugin("android.application"); }

        /**
         * Plugin provider for <b>android.library</b> with plugin id <b>com.android.library</b> and
         * with version reference <b>agp</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getLibrary() { return createPlugin("android.library"); }

    }

    public static class ComposePluginAccessors extends PluginFactory {

        public ComposePluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Plugin provider for <b>compose.compiler</b> with plugin id <b>org.jetbrains.kotlin.plugin.compose</b> and
         * with version reference <b>kotlin</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getCompiler() { return createPlugin("compose.compiler"); }

    }

    public static class FirebasePluginAccessors extends PluginFactory {

        public FirebasePluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Plugin provider for <b>firebase.crashlytics</b> with plugin id <b>com.google.firebase.crashlytics</b> and
         * with version reference <b>crashlytics</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getCrashlytics() { return createPlugin("firebase.crashlytics"); }

    }

    public static class GooglePluginAccessors extends PluginFactory {
        private final GoogleDevtoolsPluginAccessors paccForGoogleDevtoolsPluginAccessors = new GoogleDevtoolsPluginAccessors(providers, config);
        private final GoogleFirebasePluginAccessors paccForGoogleFirebasePluginAccessors = new GoogleFirebasePluginAccessors(providers, config);
        private final GoogleGmsPluginAccessors paccForGoogleGmsPluginAccessors = new GoogleGmsPluginAccessors(providers, config);

        public GooglePluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of plugins at <b>plugins.google.devtools</b>
         */
        public GoogleDevtoolsPluginAccessors getDevtools() {
            return paccForGoogleDevtoolsPluginAccessors;
        }

        /**
         * Group of plugins at <b>plugins.google.firebase</b>
         */
        public GoogleFirebasePluginAccessors getFirebase() {
            return paccForGoogleFirebasePluginAccessors;
        }

        /**
         * Group of plugins at <b>plugins.google.gms</b>
         */
        public GoogleGmsPluginAccessors getGms() {
            return paccForGoogleGmsPluginAccessors;
        }

    }

    public static class GoogleDevtoolsPluginAccessors extends PluginFactory {

        public GoogleDevtoolsPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Plugin provider for <b>google.devtools.ksp</b> with plugin id <b>com.google.devtools.ksp</b> and
         * with version <b>2.1.0-1.0.29</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getKsp() { return createPlugin("google.devtools.ksp"); }

    }

    public static class GoogleFirebasePluginAccessors extends PluginFactory {

        public GoogleFirebasePluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Plugin provider for <b>google.firebase.appdistribution</b> with plugin id <b>com.google.firebase.appdistribution</b> and
         * with version <b>5.0.0</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getAppdistribution() { return createPlugin("google.firebase.appdistribution"); }

    }

    public static class GoogleGmsPluginAccessors extends PluginFactory {
        private final GoogleGmsGooglePluginAccessors paccForGoogleGmsGooglePluginAccessors = new GoogleGmsGooglePluginAccessors(providers, config);

        public GoogleGmsPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of plugins at <b>plugins.google.gms.google</b>
         */
        public GoogleGmsGooglePluginAccessors getGoogle() {
            return paccForGoogleGmsGooglePluginAccessors;
        }

    }

    public static class GoogleGmsGooglePluginAccessors extends PluginFactory {

        public GoogleGmsGooglePluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Plugin provider for <b>google.gms.google.services</b> with plugin id <b>com.google.gms.google-services</b> and
         * with version reference <b>googleServices</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getServices() { return createPlugin("google.gms.google.services"); }

    }

    public static class JetbrainsPluginAccessors extends PluginFactory {
        private final JetbrainsKotlinPluginAccessors paccForJetbrainsKotlinPluginAccessors = new JetbrainsKotlinPluginAccessors(providers, config);

        public JetbrainsPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of plugins at <b>plugins.jetbrains.kotlin</b>
         */
        public JetbrainsKotlinPluginAccessors getKotlin() {
            return paccForJetbrainsKotlinPluginAccessors;
        }

    }

    public static class JetbrainsKotlinPluginAccessors extends PluginFactory {

        public JetbrainsKotlinPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Plugin provider for <b>jetbrains.kotlin.android</b> with plugin id <b>org.jetbrains.kotlin.android</b> and
         * with version reference <b>kotlin</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getAndroid() { return createPlugin("jetbrains.kotlin.android"); }

    }

}
