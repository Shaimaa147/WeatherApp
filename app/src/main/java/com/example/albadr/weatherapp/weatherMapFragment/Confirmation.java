package com.example.albadr.weatherapp.weatherMapFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.example.albadr.weatherapp.bookmarkListFragment.BookmarksOperations;
import com.example.albadr.weatherapp.model.PlaceInfo;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class Confirmation {

    static void confirmAddingPin(final Marker marker, final Context context, final LatLng latLng) {
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle("Add location")
                .setMessage("Are you sure you want to add this location?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final PlaceInfo place = new PlaceInfo();
                        place.setLatlng(latLng);
                        // mark
                        if (marker.getTitle() == null || marker.getTitle().equals("")) {
                            final AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                            builder2.setTitle("Location name");
                            final EditText input = new EditText(context);
                            input.setInputType(InputType.TYPE_CLASS_TEXT);
                            builder2.setView(input);
                            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!input.getText().toString().equals("")) {
                                        String city_name = input.getText().toString();
                                        marker.setTitle(city_name);
                                        place.setName(city_name);
                                        BookmarksOperations.saveLocation(place,context);
                                    } else {
                                        Toast.makeText(context, "city name cannot be empty", Toast.LENGTH_SHORT).show();
                                        builder.show();
                                    }

                                }
                            });
                            builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    marker.remove();
                                    dialog.cancel();
                                }
                            });

                            builder2.show();
                        }
                        else {
                            place.setName(marker.getTitle());
                            BookmarksOperations.saveLocation(place,context);
                        }

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        marker.remove();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }
}
