package br.com.neogis.agritopo.kml;

import android.util.Log;

import org.osmdroid.bonuspack.kml.ColorStyle;
import org.osmdroid.bonuspack.kml.IconStyle;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.KmlFolder;
import org.osmdroid.bonuspack.kml.KmlGeometry;
import org.osmdroid.bonuspack.kml.KmlGroundOverlay;
import org.osmdroid.bonuspack.kml.KmlLineString;
import org.osmdroid.bonuspack.kml.KmlMultiGeometry;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.bonuspack.kml.LineStyle;
import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.bonuspack.kml.StyleMap;
import org.osmdroid.util.GeoPoint;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by marci on 23/04/2018.
 */

public class MyKmlDocument extends KmlDocument {

    @Override
    public boolean parseKMLStream(InputStream stream, ZipFile kmzContainer) {
        MyKmlSaxHandler handler = new MyKmlSaxHandler(this.mLocalFile, kmzContainer);

        boolean ok;
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(stream, handler);
            this.mKmlRoot = handler.mKmlRoot;
            ok = true;
        } catch (Exception var6) {
            var6.printStackTrace();
            ok = false;
        }

        return ok;
    }

    protected static HashMap<String, KmlDocument.KmlKeywords> getKEYWORDS_DICTIONARY(){
        return KEYWORDS_DICTIONARY;
    }

    protected class MyKmlSaxHandler extends DefaultHandler {

        private StringBuilder mStringBuilder = new StringBuilder(1024);
        private KmlFeature mKmlCurrentFeature;
        private KmlGroundOverlay mKmlCurrentGroundOverlay; //if GroundOverlay, pointer to mKmlCurrentFeature
        private ArrayList<KmlFeature> mKmlFeatureStack;
        private KmlGeometry mKmlCurrentGeometry;
        private ArrayList<KmlGeometry> mKmlGeometryStack;
        public KmlFolder mKmlRoot;
        Style mCurrentStyle;
        String mCurrentStyleId;
        StyleMap mCurrentStyleMap; //for StyleSelector: "normal" or "highlight"
        String mCurrentStyleKey;
        ColorStyle mColorStyle;
        String mDataName;
        boolean mIsNetworkLink;
        boolean mIsInnerBoundary;
        File mFile; //to get the path of relative sub-files
        ZipFile mKMZFile;
        double mNorth, mEast, mSouth, mWest;

        public MyKmlSaxHandler(File file, ZipFile kmzContainer){
            mFile = file;
            mKMZFile = kmzContainer;
            mKmlRoot = new KmlFolder();
            mKmlFeatureStack = new ArrayList<KmlFeature>();
            mKmlFeatureStack.add(mKmlRoot);
            mKmlGeometryStack = new ArrayList<KmlGeometry>();
            mIsNetworkLink = false;
            mIsInnerBoundary = false;
        }

        protected void loadNetworkLink(String href, ZipFile kmzContainer){
            MyKmlDocument subDocument = new MyKmlDocument();
            boolean ok;
            if (href.startsWith("http://") || href.startsWith("https://") )
                ok = subDocument.parseKMLUrl(href);
            else if (kmzContainer == null){
                File subFile = new File(mFile.getParent()+'/'+href);
                ok = subDocument.parseKMLFile(subFile);
            } else {
                try {
                    final ZipEntry fileEntry = kmzContainer.getEntry(href);
                    InputStream stream = kmzContainer.getInputStream(fileEntry);
                    Log.d("MyKmlSaxHandler", "Load NetworkLink:"+href);
                    ok = subDocument.parseKMLStream(stream, kmzContainer);
                } catch (Exception e) {
                    ok = false;
                }
            }
            if (ok){
                //add subDoc root to the current feature, which is -normally- the NetworkLink:
                ((KmlFolder)mKmlCurrentFeature).add(subDocument.mKmlRoot);
                //add all subDocument styles to mStyles:
                mStyles.putAll(subDocument.getStyles());
            } else {
                Log.e("MyKmlSaxHandler", "Error reading NetworkLink:"+href);
            }
        }

        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {
            if (localName.equals("Document")){
                mKmlCurrentFeature = mKmlRoot; //If there is a Document, it will be the root.
                mKmlCurrentFeature.mId = attributes.getValue("id");
            } else if (localName.equals("Folder")){
                mKmlCurrentFeature = new KmlFolder();
                mKmlCurrentFeature.mId = attributes.getValue("id");
                mKmlFeatureStack.add(mKmlCurrentFeature); //push on stack
            } else if (localName.equals("NetworkLink")){
                mKmlCurrentFeature = new KmlFolder();
                mKmlCurrentFeature.mId = attributes.getValue("id");
                mKmlFeatureStack.add(mKmlCurrentFeature); //push on stack
                mIsNetworkLink = true;
            } else if (localName.equals("GroundOverlay")){
                mKmlCurrentGroundOverlay = new KmlGroundOverlay();
                mKmlCurrentFeature = mKmlCurrentGroundOverlay;
                mKmlCurrentFeature.mId = attributes.getValue("id");
                mKmlFeatureStack.add(mKmlCurrentFeature); //push on stack
            } else if (localName.equals("Placemark")) {
                mKmlCurrentFeature = new KmlPlacemark();
                mKmlCurrentFeature.mId = attributes.getValue("id");
                mKmlFeatureStack.add(mKmlCurrentFeature); //push on Feature stack
            } else if (localName.equals("Point")){
                mKmlCurrentGeometry = new MyKmlPoint();
                mKmlGeometryStack.add(mKmlCurrentGeometry); //push on Geometry stack
            } else if (localName.equals("LineString")){
                mKmlCurrentGeometry = new MyKmlLineString();
                mKmlGeometryStack.add(mKmlCurrentGeometry);
            } else if (localName.equals("Polygon")){
                mKmlCurrentGeometry = new MyKmlPolygon();
                mKmlGeometryStack.add(mKmlCurrentGeometry);
            } else if (localName.equals("innerBoundaryIs")) {
                mIsInnerBoundary = true;
            } else if (localName.equals("MultiGeometry")){
                mKmlCurrentGeometry = new KmlMultiGeometry();
                mKmlGeometryStack.add(mKmlCurrentGeometry);
            } else if (localName.equals("Style")) {
                mCurrentStyle = new Style();
                mCurrentStyleId = attributes.getValue("id");
            } else if (localName.equals("StyleMap")) {
                mCurrentStyleMap = new StyleMap();
                mCurrentStyleId = attributes.getValue("id");
            } else if (localName.equals("LineStyle")) {
                mCurrentStyle.mLineStyle = new LineStyle();
                mColorStyle = mCurrentStyle.mLineStyle;
            } else if (localName.equals("PolyStyle")) {
                mCurrentStyle.mPolyStyle = new ColorStyle();
                mColorStyle = mCurrentStyle.mPolyStyle;
            } else if (localName.equals("IconStyle")) {
                mCurrentStyle.mIconStyle = new IconStyle();
                mColorStyle = mCurrentStyle.mIconStyle;
            } else if (localName.equals("hotSpot")){
                if (mCurrentStyle != null && mCurrentStyle.mIconStyle != null){
                    mCurrentStyle.mIconStyle.mHotSpot.mx = Float.parseFloat(attributes.getValue("x"));
                    mCurrentStyle.mIconStyle.mHotSpot.my = Float.parseFloat(attributes.getValue("y"));
                }
            } else if (localName.equals("Data") || localName.equals("SimpleData")) {
                mDataName = attributes.getValue("name");
            }
            mStringBuilder.setLength(0);
        }

        public @Override void characters(char[] ch, int start, int length)
                throws SAXException {
            mStringBuilder.append(ch, start, length);
        }

        public void endElement(String uri, String localName, String name)
                throws SAXException {
            if (localName.equals("Document")){
                //Document is the root, nothing to do.
            } else if (localName.equals("Folder") || localName.equals("Placemark")
                    || localName.equals("NetworkLink") || localName.equals("GroundOverlay")) {
                //this was a Feature:
                KmlFolder parent = (KmlFolder)mKmlFeatureStack.get(mKmlFeatureStack.size()-2); //get parent
                parent.add(mKmlCurrentFeature); //add current in its parent
                mKmlFeatureStack.remove(mKmlFeatureStack.size()-1); //pop current from stack
                mKmlCurrentFeature = mKmlFeatureStack.get(mKmlFeatureStack.size()-1); //set current to top of stack
                if (localName.equals("NetworkLink"))
                    mIsNetworkLink = false;
                else if (localName.equals("GroundOverlay"))
                    mKmlCurrentGroundOverlay = null;
            } else if (localName.equals("innerBoundaryIs")){
                mIsInnerBoundary = false;
            } else if (localName.equals("Point") || localName.equals("LineString") || localName.equals("Polygon")
                    || localName.equals("MultiGeometry") ){
                //this was a Geometry:
                if (mKmlGeometryStack.size() == 1){
                    //no MultiGeometry parent: add this Geometry in the current Feature:
                    ((KmlPlacemark)mKmlCurrentFeature).mGeometry = mKmlCurrentGeometry;
                    mKmlGeometryStack.remove(mKmlGeometryStack.size()-1); //pop current from stack
                    mKmlCurrentGeometry = null;
                } else {
                    KmlMultiGeometry parent = (KmlMultiGeometry)mKmlGeometryStack.get(mKmlGeometryStack.size()-2); //get parent
                    parent.addItem(mKmlCurrentGeometry); //add current in its parent
                    mKmlGeometryStack.remove(mKmlGeometryStack.size()-1); //pop current from stack
                    mKmlCurrentGeometry = mKmlGeometryStack.get(mKmlGeometryStack.size()-1); //set current to top of stack
                }
            } else if (localName.equals("name")){
                mKmlCurrentFeature.mName = mStringBuilder.toString();
            } else if (localName.equals("description")){
                mKmlCurrentFeature.mDescription = mStringBuilder.toString();
            } else if (localName.equals("visibility")){
                mKmlCurrentFeature.mVisibility = ("1".equals(mStringBuilder.toString()));
            } else if (localName.equals("open")){
                mKmlCurrentFeature.mOpen = ("1".equals(mStringBuilder.toString()));
            } else if (localName.equals("coordinates")){
                if (mKmlCurrentFeature instanceof KmlPlacemark){
                    if (!mIsInnerBoundary){
                        mKmlCurrentGeometry.mCoordinates = parseKmlCoordinates(mStringBuilder.toString());
                        //mKmlCurrentFeature.mBB = BoundingBoxE6.fromGeoPoints(mKmlCurrentGeometry.mCoordinates);
                    } else { //inside a Polygon innerBoundaryIs element: new hole
                        MyKmlPolygon polygon = (MyKmlPolygon)mKmlCurrentGeometry;
                        if (polygon.mHoles == null)
                            polygon.mHoles = new ArrayList<ArrayList<GeoPoint>>();
                        ArrayList<GeoPoint> hole = parseKmlCoordinates(mStringBuilder.toString());
                        polygon.mHoles.add(hole);
                    }
                }
            } else if (localName.equals("styleUrl")){
                String styleUrl;
                if (mStringBuilder.charAt(0) == '#')
                    styleUrl = mStringBuilder.substring(1); //remove the #
                else //external url: keep as is:
                    styleUrl = mStringBuilder.toString();

                if (mCurrentStyleMap != null){
                    mCurrentStyleMap.setPair(mCurrentStyleKey, styleUrl);
                } else if (mKmlCurrentFeature != null){
                    mKmlCurrentFeature.mStyle = styleUrl;
                }
            } else if (localName.equals("key")){
                mCurrentStyleKey = mStringBuilder.toString();
            } else if (localName.equals("color")){
                if (mCurrentStyle != null) {
                    if (mColorStyle != null)
                        mColorStyle.mColor = ColorStyle.parseKMLColor(mStringBuilder.toString());
                } else if (mKmlCurrentGroundOverlay != null){
                    mKmlCurrentGroundOverlay.mColor = ColorStyle.parseKMLColor(mStringBuilder.toString());
                }
            } else if (localName.equals("colorMode")){
                if (mCurrentStyle != null && mColorStyle != null)
                    mColorStyle.mColorMode = (mStringBuilder.toString().equals("random")?1:0);
            } else if (localName.equals("width")){
                if (mCurrentStyle != null && mCurrentStyle.mLineStyle != null)
                    mCurrentStyle.mLineStyle.mWidth = Float.parseFloat(mStringBuilder.toString());
            } else if (localName.equals("scale")){
                if (mCurrentStyle != null && mCurrentStyle.mIconStyle != null){
                    mCurrentStyle.mIconStyle.mScale = Float.parseFloat(mStringBuilder.toString());
                }
            } else if (localName.equals("heading")){
                if (mCurrentStyle != null && mCurrentStyle.mIconStyle != null){
                    mCurrentStyle.mIconStyle.mHeading = Float.parseFloat(mStringBuilder.toString());
                }
            } else if (localName.equals("href")){
                if (mCurrentStyle != null && mCurrentStyle.mIconStyle != null){
                    //href of an Icon in an IconStyle:
                    String href = mStringBuilder.toString();
                    mCurrentStyle.setIcon(href, mFile, mKMZFile);
                } else if (mIsNetworkLink){
                    //href of a NetworkLink:
                    String href = mStringBuilder.toString();
                    loadNetworkLink(href, mKMZFile);
                } else if (mKmlCurrentGroundOverlay != null){
                    //href of a GroundOverlay Icon:
                    mKmlCurrentGroundOverlay.setIcon(mStringBuilder.toString(), mFile, mKMZFile);
                }
            } else if (localName.equals("Style")){
                if (mCurrentStyleId != null)
                    putStyle(mCurrentStyleId, mCurrentStyle);
                else {
                    mCurrentStyleId = addStyle(mCurrentStyle);
                    if (mKmlCurrentFeature != null){
                        //this is an inline style. Set its style id to the KmlObject container:
                        mKmlCurrentFeature.mStyle = mCurrentStyleId;
                    }
                }
                mCurrentStyle = null;
                mCurrentStyleId = null;
            } else if (localName.equals("StyleMap")){
                if (mCurrentStyleId != null)
                    putStyle(mCurrentStyleId, mCurrentStyleMap);
                //TODO: inline StyleMap ???
                mCurrentStyleMap = null;
                mCurrentStyleId = null;
                mCurrentStyleKey = null;
            } else if (localName.equals("north")){
                mNorth = Double.parseDouble(mStringBuilder.toString());
            } else if (localName.equals("south")){
                mSouth = Double.parseDouble(mStringBuilder.toString());
            } else if (localName.equals("east")){
                mEast = Double.parseDouble(mStringBuilder.toString());
            } else if (localName.equals("west")){
                mWest = Double.parseDouble(mStringBuilder.toString());
            } else if (localName.equals("rotation")){
                mKmlCurrentGroundOverlay.mRotation = Float.parseFloat(mStringBuilder.toString());
            } else if (localName.equals("LatLonBox")){
                if (mKmlCurrentGroundOverlay != null){
                    mKmlCurrentGroundOverlay.setLatLonBox(mNorth, mSouth, mEast, mWest);
                }
            } else if (localName.equals("SimpleData")){
                //We don't check the schema from SchemaData. We just pick the name and the value from SimpleData:
                mKmlCurrentFeature.setExtendedData(mDataName, mStringBuilder.toString());
                mDataName = null;
            } else if (localName.equals("value")){
                mKmlCurrentFeature.setExtendedData(mDataName, mStringBuilder.toString());
                mDataName = null;
            }
        }

    }
}
