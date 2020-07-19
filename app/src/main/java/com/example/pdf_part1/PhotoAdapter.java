package com.example.pdf_part1;

import android.app.AppComponentFactory;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private Context mContext;
    private ArrayList<Uri> listData;
    int i = 0,flag=0;
    String fout=null;
    Button crop_image;
    PdfDocument pdfDocument1 = new PdfDocument();

    public PhotoAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setDataPhoto(ArrayList<Uri> listUri) {
        this.listData = listUri;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);

        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Uri uri = listData.get(position);
        try {
           final Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            holder.imgPhoto.setImageBitmap(bitmap);
            PDFMaker(bitmap,i);
            i++;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return null == listData ? 0 : listData.size();
    }

   public class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPhoto;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_photo);

        }
    }

   public Bitmap getResizedBitmap(Bitmap bitmap0,int maxsize)
    {
      int width=bitmap0.getWidth();
      int height=bitmap0.getHeight();
      float ratio=(float)width/(float)height;
      if(ratio>1)
      {
          width=maxsize;
          height=(int)(width/ratio);
      }
      else
      {
          height=maxsize;
          width=(int)(height*ratio);
      }
      return Bitmap.createScaledBitmap(bitmap0,width,height,true);
    }


    public void PDFMaker(Bitmap bitmap1,int i) {
        if(i<listData.size()) {

            Bitmap bitmap2=getResizedBitmap(bitmap1,600);
            PdfDocument.PageInfo pii = new PdfDocument.PageInfo.Builder(bitmap2.getWidth(),
                    bitmap2.getHeight(), 1).create();
            PdfDocument.Page page1 = pdfDocument1.startPage(pii);
            Canvas canvas = page1.getCanvas();
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#FFFFFF"));
            canvas.drawPaint(paint);
           // bitmap2 = Bitmap.createScaledBitmap(bitmap2, bitmap2.getWidth(), bitmap2.getHeight(), true);
            paint.setColor(Color.BLACK);
            canvas.drawBitmap(bitmap2, 0, 0, null);
            pdfDocument1.finishPage(page1);



            if (i == listData.size()-1) {

                Finish();

                }
        }
    }

    public void Finish() {
        File root = new File(Environment.getExternalStorageDirectory(), "pdfFolder");
        if(!root.exists())
        {
            root.mkdir();
        }
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date=df.format(Calendar.getInstance().getTime());
        File file= new File(root,"New PDF "+date+".pdf");
        try{
            FileOutputStream out = new FileOutputStream(file);
            pdfDocument1.writeTo(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        pdfDocument1.close();
        Toast.makeText(mContext,"pdf is saved "+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();

    }



}





