package edu.uw.xfchu.matrix;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ViewSwitcher;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    private View mView;
    private GoogleMap mMap;
    private LocationTracker locationTracker;
    private FloatingActionButton fab_report;
    private FloatingActionButton fab_focus;
    private Dialog dialog;
    private RecyclerView mRecyclerView;
    private ReportRecyclerViewAdapter mRecyclerViewAdapter;
    private ViewSwitcher mViewSwitcher;
    private String event_type = null;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment mainFragment = new MainFragment();
        return mainFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_main, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final MainFragment fragment = this;
        mMapView = (MapView) mView.findViewById(R.id.event_map_view);
        fab_report = (FloatingActionButton) mView.findViewById(R.id.fab);
        fab_focus = (FloatingActionButton) mView.findViewById(R.id.fab_focus);

        fab_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show dialog
                showDialog();
            }
        });

        fab_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapView.getMapAsync(fragment);
            }
        });

        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume(); // needed to get the map to display immediately
            mMapView.getMapAsync(this);
        }
    }

    // Set up type items
    private void setupRecyclerView(View dialogView) {
        mRecyclerView = dialogView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        List<Item> listItems = new ArrayList<>();
        listItems.add(new Item(Config.POLICE, R.drawable.policeman));
        listItems.add(new Item(Config.TRAFFIC, R.drawable.traffic));
        listItems.add(new Item(Config.NO_ENTRY, R.drawable.no_entry));
        listItems.add(new Item(Config.NO_PARKING, R.drawable.no_parking));
        listItems.add(new Item(Config.SECURITY_CAMERA, R.drawable.security_camera));
        listItems.add(new Item(Config.HEADLIGHT, R.drawable.lights));
        listItems.add(new Item(Config.SPEEDING, R.drawable.speeding));
        listItems.add(new Item(Config.CONSTRUCTION, R.drawable.construction));
        listItems.add(new Item(Config.SLIPPERY, R.drawable.slippery));

        mRecyclerViewAdapter = new ReportRecyclerViewAdapter(getActivity(), listItems);
        mRecyclerViewAdapter.setClickListener(new ReportRecyclerViewAdapter.OnClickListener() {
            @Override
            public void setItem(String item) {
                event_type = item;
                if (mViewSwitcher != null) {
                    mViewSwitcher.showNext();
                }
            }
        });
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }


    // Animation show dialog
    private void showDialog() {
        final View dialogView = View.inflate(getActivity(), R.layout.dialog, null);
        dialog = new Dialog(getActivity(), R.style.MyAlertDialogStyle);
        mViewSwitcher = (ViewSwitcher) dialogView.findViewById(R.id.viewSwitcher);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                animateDialog(dialogView, true, null);
            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    animateDialog(dialogView, false, dialog);
                    return true;
                }
                return false;
            }
        });

        dialog.getWindow().setBackgroundDrawable(new
                ColorDrawable(Color.TRANSPARENT));
        setupRecyclerView(dialogView);
        dialog.show();
    }

    // Add animation to Floating Action Button
    private void animateDialog(View dialogView, boolean open, final Dialog dialog) {
        final View view = dialogView.findViewById(R.id.dialog);
        int w = view.getWidth();
        int h = view.getHeight();

        int endRadius = (int) Math.hypot(w, h);

        int cx = (int) (fab_report.getX() + (fab_report.getWidth() / 2));
        int cy = (int) (fab_report.getY()) + fab_report.getHeight() + 56;

        if (open) {
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0,
                    endRadius);
            view.setVisibility(View.VISIBLE);
            revealAnimator.setDuration(500);
            revealAnimator.start();
        } else {
            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, endRadius,
                    0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);
                }
            });
            anim.setDuration(500);
            anim.start();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mMap = googleMap;
        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.style_json)
        );
//        double latitude = 47.609250;
//        double longitude = -122.203440;

        locationTracker = new LocationTracker(getActivity());
        locationTracker.getLocation();
        LatLng latLng = new LatLng(locationTracker.getLatitude(),
                locationTracker.getLongitude());

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng) // Sets the center of the map to location
                .zoom(16) // Sets the zoom
                .bearing(90) // Sets the orientation of the camera to east
                .tilt(30) // Sets the tilt of the camera to 30 degrees
                .build(); // Creates a CameraPosition from the builder

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // Create marker on Google map
        MarkerOptions marker = new MarkerOptions().position(
                latLng).title("You");

        // Change marker Icon on google map
        marker.icon(BitmapDescriptorFactory.
                fromResource(R.drawable.boy));

        // Add marker to google map
        Marker mker = googleMap.addMarker(marker);

    }
}
