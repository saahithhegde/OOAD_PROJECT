import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SellBooksComponent } from './sell-books.component';

describe('SellBooksComponent', () => {
  let component: SellBooksComponent;
  let fixture: ComponentFixture<SellBooksComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SellBooksComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SellBooksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
