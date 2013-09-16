package me.key.appmarket.widgets;

import com.market.d9game.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自定义 ViewGroup
 * 
 * @author yutinglong
 */
public class FlowLayout extends ViewGroup {
	private int mHorizontalSpacing = 10;
	private int mVerticalSpacing = 10;

	private Paint mPaint;

	public FlowLayout(Context context) {
		this(context, null);
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(0xffff0000);
		mPaint.setStrokeWidth(2.0f);
	}

	public int getmHorizontalSpacing() {
		return mHorizontalSpacing;
	}

	public void setmHorizontalSpacing(int mHorizontalSpacing) {
		this.mHorizontalSpacing = mHorizontalSpacing;
	}

	public int getmVerticalSpacing() {
		return mVerticalSpacing;
	}

	public void setmVerticalSpacing(int mVerticalSpacing) {
		this.mVerticalSpacing = mVerticalSpacing;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec)
				- getPaddingRight();
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);

		boolean growHeight = widthMode != MeasureSpec.UNSPECIFIED;

		int width = 0;
		int height = getPaddingTop();

		int currentWidth = getPaddingLeft(); // 当前行的宽度
		int currentHeight = 0; // 当前行的高度

		boolean breakLine = false;
		boolean newLine = false;
		int spacing = 0;

		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			measureChild(child, widthMeasureSpec, heightMeasureSpec);

			LayoutParams lp = (LayoutParams) child.getLayoutParams();
			spacing = mHorizontalSpacing;

			if (growHeight
					&& (breakLine || currentWidth + child.getMeasuredWidth() > widthSize)) {
				newLine = true;

				height += currentHeight + mVerticalSpacing;
				width = Math.max(width, currentWidth - spacing);

				currentHeight = 0;
				currentWidth = getPaddingLeft();

			} else {
				newLine = false;
			}

			lp.x = currentWidth;
			lp.y = height;

			currentWidth += child.getMeasuredWidth() + spacing;
			currentHeight = Math.max(currentHeight, child.getMeasuredHeight());

			breakLine = lp.breakLine;
		}

		if (!newLine) {
			height += currentHeight;
			width = Math.max(width, currentWidth - spacing);
		}

		width += getPaddingRight();
		height += getPaddingBottom();

		setMeasuredDimension(resolveSize(width, widthMeasureSpec),
				resolveSize(height, heightMeasureSpec));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			LayoutParams lp = (LayoutParams) child.getLayoutParams();
			child.layout(lp.x, lp.y, lp.x + child.getMeasuredWidth(), lp.y
					+ child.getMeasuredHeight());
		}
	}

	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		boolean more = super.drawChild(canvas, child, drawingTime);
		LayoutParams lp = (LayoutParams) child.getLayoutParams();
		if (lp.horizontalSpacing > 0) {
			float x = child.getRight();
			float y = child.getTop() + child.getHeight() / 2.0f;
			canvas.drawLine(x, y - 4.0f, x, y + 4.0f, mPaint);
			canvas.drawLine(x, y, x + lp.horizontalSpacing, y, mPaint);
			canvas.drawLine(x + lp.horizontalSpacing, y - 4.0f, x
					+ lp.horizontalSpacing, y + 4.0f, mPaint);
		}
		if (lp.breakLine) {
			float x = child.getRight();
			float y = child.getTop() + child.getHeight() / 2.0f;
			canvas.drawLine(x, y, x, y + 6.0f, mPaint);
			canvas.drawLine(x, y + 6.0f, x + 6.0f, y + 6.0f, mPaint);
		}
		return more;
	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams;
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	@Override
	protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return new LayoutParams(p.width, p.height);
	}

	public static class LayoutParams extends ViewGroup.LayoutParams {
		int x;
		int y;

		public int horizontalSpacing;
		public boolean breakLine;

		public LayoutParams(Context context, AttributeSet attrs) {
			super(context, attrs);
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.FlowLayout_LayoutParams);
			try {
				horizontalSpacing = a
						.getDimensionPixelSize(
								R.styleable.FlowLayout_LayoutParams_layout_horizontalSpacing,
								-1);
				breakLine = a.getBoolean(
						R.styleable.FlowLayout_LayoutParams_layout_breakLine,
						false);
			} finally {
				a.recycle();
			}
		}

		public LayoutParams(int w, int h) {
			super(w, h);
		}
	}
}
